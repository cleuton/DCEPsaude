package org.conserpro2015.saude;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
//java -jar saude-0.0.1-SNAPSHOT.jar server /Users/cleutonsampaio/Documents/projetos/HPP/ws/Conserpro2015/WS/saude/src/main/resources/saude.yml
public class TestSintoma {
	 HttpClient httpclient = new DefaultHttpClient();
	@Test
	/*
	 * var regioes = [ "Região inválida",
               	"Rio de Janeiro - Zona Sul",
               	"Rio de Janeiro - Zona Norte", 
               	"Rio de Janeiro - Zona Oeste",
               	"Rio de Janeiro - Leopondina",
               	"Duque de Caxias",
               	"Niterói",
               	"Nova Iguaçú",
               	"São Gonçalo",
               	"Queimados"
              ];
var sintomas = ["Sintoma inválido",
                "Dor de cabeça",
                "Enjôo",
                "Vômito",
                "Diarréia",
                "Febre",
                "Dor muscular",
                "Rigidez na nuca",
                "Dor abdominal"
                ];
	 */
	public void test() {
		sendRequest("{\"regiao\":1,\"sintoma\":1,\"data\":\"2015-07-20 10:40:00\"}");
		sendRequest("{\"regiao\":1,\"sintoma\":1,\"data\":\"2015-07-22 10:40:00\"}");
		sendRequest("{\"regiao\":3,\"sintoma\":1,\"data\":\"2015-07-22 10:40:00\"}");
		sendRequest("{\"regiao\":3,\"sintoma\":4,\"data\":\"2015-07-23 10:40:00\"}");
		sendRequest("{\"regiao\":3,\"sintoma\":4,\"data\":\"2015-07-25 10:40:00\"}");
		sendRequest("{\"regiao\":7,\"sintoma\":2,\"data\":\"2015-07-20 10:40:00\"}");
		sendRequest("{\"regiao\":2,\"sintoma\":2,\"data\":\"2015-07-21 10:40:00\"}");
		sendRequest("{\"regiao\":9,\"sintoma\":3,\"data\":\"2015-07-25 10:40:00\"}");
		sendRequest("{\"regiao\":9,\"sintoma\":4,\"data\":\"2015-07-25 10:40:00\"}");
		sendRequest("{\"regiao\":9,\"sintoma\":4,\"data\":\"2015-07-25 10:40:00\"}");
		sendRequest("{\"regiao\":9,\"sintoma\":4,\"data\":\"2015-07-25 10:40:00\"}");
		sendRequest("{\"regiao\":9,\"sintoma\":4,\"data\":\"2015-07-25 10:40:00\"}");
	}
	
	void sendRequest(String sentity) {
	    try {
		      HttpHost target = new HttpHost("localhost", 3000, "http");
		      HttpPost postRequest = new HttpPost(
		  			"/api/sintoma");
		   
		  		StringEntity input = new StringEntity(sentity);
		  		input.setContentType("application/json");
		  		postRequest.setEntity(input);
		 
		      System.out.println("executing request to " + target);
		 
		      HttpResponse httpResponse = httpclient.execute(target, postRequest);
		      HttpEntity entity = httpResponse.getEntity();
		 
		      System.out.println("----------------------------------------");
		      System.out.println(httpResponse.getStatusLine());
		      Header[] headers = httpResponse.getAllHeaders();
		      for (int i = 0; i < headers.length; i++) {
		        System.out.println(headers[i]);
		      }
		      System.out.println("----------------------------------------");
		 
		      if (entity != null) {
		        System.out.println(EntityUtils.toString(entity));
		      }
		 
		    } catch (Exception e) {
		      e.printStackTrace();
		    } 
	}

}
