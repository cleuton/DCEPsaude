package org.conserpro2015.saude;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.codahale.metrics.annotation.Timed;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.obomprogramador.microservice.servkeeper.ServiceClient.Increment;

@Path("/sintoma")
public class SaudeResource {
	private String dbUrl;
	private String zkServerAddress;
	private Increment increment;
	private Logger logger = Logger.getLogger(this.getClass());
	private MongoClient client;
	@POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public Response verify(Sintoma sintoma) {
    	boolean resultado = false;
    	String mensagem = "";
    	String status = "";
    	int httpStatus = 200;
    	try {
    		if(insertSintoma(sintoma)) {
    			status = "success";
    			mensagem = "sintoma inserido";
    		}
    		else {
    			status = "falha";
    			mensagem = "erro ao inserir";
    			httpStatus = 500;
    		}
    		this.increment.increment();
		} catch (Exception e) {
			status = "error";
			mensagem = e.getClass().getName() + ": " + e.getLocalizedMessage();
			httpStatus = 500;
		} 

    	String saidaJSON = "{ \"status\": \"" + status + "\","
    					 + "\"data\": {"
    					 + "\"mensagem\": \"" + mensagem + "\"}}"; 
        return Response
        		.status(httpStatus)
        		.header("Access-Control-Allow-Origin", "*")
        		.entity(saidaJSON).build();
    }

	public SaudeResource(String dbUrl, 
						 String zkServerAddress) {
		this.dbUrl = dbUrl;
		this.zkServerAddress = zkServerAddress;
		this.increment = new Increment(this.zkServerAddress, "/saudeserver_counter");
		logger.info("### Starting resource SaudeResource. dbUrl: "
				+ this.dbUrl
				+ ", zkServer: "
				+ this.zkServerAddress);

	}
	
	private boolean insertSintoma(Sintoma sintoma) {
		boolean retorno = false;
		MongoDatabase db = null;
		try {
			if (this.client == null) {
				openDB();
			}
			db = this.client.getDatabase("dbsaude");
			MongoCollection<Document> collection = db.getCollection("sintoma");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date dataSintoma = formatter.parse(sintoma.getData());
			Document doc = new Document("regiao", sintoma.getRegiao())
            	.append("sintoma", sintoma.getSintoma())
            	.append("data", dataSintoma);
			collection.insertOne(doc);
			retorno = true;
		}
		catch (Exception ex) {
			logger.error("Exception salvando sintoma: " + ex.getMessage());
			retorno = false;
		}
		return retorno;
	}

	private void openDB() throws Exception {
		this.client = new MongoClient(this.dbUrl);
	}
	
}

