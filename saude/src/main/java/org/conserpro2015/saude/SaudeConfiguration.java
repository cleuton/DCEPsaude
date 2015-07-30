package org.conserpro2015.saude;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class SaudeConfiguration extends Configuration {
	private String dbUrl;

	@JsonProperty
	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	} 

	

	
	
}

