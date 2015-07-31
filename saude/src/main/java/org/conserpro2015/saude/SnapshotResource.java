package org.conserpro2015.saude;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.codahale.metrics.annotation.Timed;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOptions;
import com.obomprogramador.microservice.servkeeper.ServiceClient.Increment;

@Path("/snapshot")
public class SnapshotResource {
	private String dbUrl;
	private String zkServerAddress;
	private Increment increment;
	private Logger logger = Logger.getLogger(this.getClass());
	private MongoClient client;
	@GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
	/**
	 * Retorna o MapReduce dos sintomas apurados
	 * @param regiao Código da região (IBGE)
	 * @param dtInicio aaaa-mm-dd
	 * @param dtFim    aaaa-mm-dd
	 * @return
	 */
    public Response create(
    		@DefaultValue("0") @QueryParam("regiao") int regiao,
    		@DefaultValue("*") @QueryParam("dtInicio") String dtInicio,
			@DefaultValue("*") @QueryParam("dtFim") String dtFim) {
    	String mensagem = "";
    	String status = "";
    	int httpStatus = 200;
    	try {
    		mensagem = geraMapReduce(regiao, dtInicio, dtFim);
    		this.increment.increment();
		} catch (Exception e) {
			status = "error";
			mensagem = e.getClass().getName() + ": " + e.getLocalizedMessage();
			httpStatus = 500;
		} 

    	String saidaJSON = "{ \"status\": \"" + status + "\","
    					 + "\"data\": {"
    					 + "\"mensagem\": " + mensagem + "}}"; 
        return Response
        		.status(httpStatus)
        		.header("Access-Control-Allow-Origin", "*")
        		.entity(saidaJSON).build();
    }

	private String geraMapReduce(int regiao, String dtInicio, String dtFim) throws Exception {
		String JsonResposta = null;
		MongoDatabase db = null;
		if (this.client == null) {
			openDB();
		}
		db = this.client.getDatabase("dbsaude");
		   String map = "function() { "+
		             "emit(this.sintoma, 1);}";
		    
		   String reduce = "function(key, values) { " +
		                            "var sum = 0; " +
		                            "values.forEach(function(doc) { " +
		                            "sum += 1; "+
		                            "}); " +
		                            "return sum;} ";

		   MongoCollection<Document> collection = db.getCollection("sintoma");
		   MapReduceIterable<Document> saida = null;
		   // Caso 1: Sem parâmetros: 
		   if (regiao == 0 && dtInicio.equals("*")) {
			   saida = collection.mapReduce(map,reduce);
		   }
		   // Caso 2: Só região
		   else if (regiao != 0 && dtInicio.equals("*")) {
			   	saida = collection.mapReduce(map,reduce).filter(new Document("regiao", regiao));
		   }
		   else {
			   String sDtInicio = dtInicio + " 00:00:00";
			   String sDtFim = dtFim + " 23:59:00";
			   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			   Date convertDtInicio = formatter.parse(sDtInicio);
			   Date convertDtFim = formatter.parse(sDtFim);
			   if (regiao == 0) {
				   saida = collection.mapReduce(map,reduce)
						   .filter(new Document("data", new Document("$gte",convertDtInicio).append("$lte", convertDtFim)));

			   }
			   else {
				   saida = collection.mapReduce(map,reduce)
						   .filter(new Document("regiao", regiao)
						   			.append("data", new Document("$gte",convertDtInicio))
						   			.append("data", new Document("$lte",convertDtFim))
								   );
			   }
		   }
		   JsonResposta = "[";
		   for (Document doc : saida) {
			   if (JsonResposta.length() > 1) {
				   JsonResposta += ",";
			   }
			   JsonResposta += doc.toJson();
		   }
		   JsonResposta += "]";

		return JsonResposta;
	}

	public SnapshotResource(String dbUrl, 
			 String zkServerAddress) {
		this.dbUrl = dbUrl;
		this.zkServerAddress = zkServerAddress;
		this.increment = new Increment(this.zkServerAddress, "/saudeserver_counter");
		logger.info("### Starting resource SaudeResource. dbUrl: "
				+ this.dbUrl
				+ ", zkServer: "
				+ this.zkServerAddress);

	}
	
	private void openDB() throws Exception {
		this.client = new MongoClient(this.dbUrl);
	}
}

