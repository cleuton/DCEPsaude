package org.conserpro2015.saude;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.codahale.metrics.annotation.Timed;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.obomprogramador.microservice.servkeeper.ServiceClient.Increment;

@Path("/status")
public class CheckResource {
	private String dbUrl;
	private String zkServerAddress;
	//private Increment increment;
	private Logger logger = Logger.getLogger(this.getClass());
	private MongoClient client;
	@GET
    @Timed
	@Produces(MediaType.APPLICATION_JSON)
    public Response checkStatus() {
    	boolean resultado = false;
    	String mensagem = "";
    	String status = "";
    	int httpStatus = 200;
    	try {
    		if(this.verifyDb()) {
    			status = "ok";
    			mensagem = "status ok";
    		}
    		else {
    			status = "falha";
    			mensagem = "erro";
    			httpStatus = 500;
    		}
    	
		} catch (Exception e) {
			status = "error";
			mensagem = e.getClass().getName() + ": " + e.getLocalizedMessage();
			httpStatus = 500;
		} 

    	String saidaJSON = "{ \"status\": \"" + status + "\","
    					 + "\"data\": {"
    					 + "\"mensagem\": \"" + mensagem + "\"}}"; 
        return Response.status(httpStatus).entity(saidaJSON).build();

	}
	
	public CheckResource(String dbUrl, 
			String zkServerAddress) {
		super();
		this.dbUrl = dbUrl;
		/*
		this.zkServerAddress = zkServerAddress;
		this.increment = new Increment(this.zkServerAddress, "/saudeserver_counter");
		*/
	}	
	
	private boolean verifyDb() {
		boolean retorno = false;
		MongoDatabase db = null;
		try {
			if (this.client == null) {
				openDB();
			}
			db = this.client.getDatabase("dbsaude");
			long contagem = db.getCollection("sintoma").count();
			logger.debug("# Check - Contagem de Sintomas: " + contagem);
			retorno = true;
		}
		catch (Exception ex) {
			logger.error("Exception verificando o sistema: " + ex.getMessage());
			retorno = false;
		}
		return retorno;
	}

	private void openDB() throws Exception {
		this.client = new MongoClient(this.dbUrl);
	}


}