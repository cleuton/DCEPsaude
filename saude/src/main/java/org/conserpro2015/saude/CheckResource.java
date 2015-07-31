package org.conserpro2015.saude;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.codahale.metrics.annotation.Timed;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

@Path("/status")
public class CheckResource {
	private String dbUrl;
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
        return Response
        		.status(httpStatus)
        		.header("Access-Control-Allow-Origin", "*")
        		.entity(saidaJSON).build();

	}
	
	public CheckResource(String dbUrl) {
		super();
		this.dbUrl = dbUrl;
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
