package org.conserpro2015.saude;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class SaudeConfiguration extends Configuration {
	private String dbUrl;
	private String zkServerAddress;
	
	@JsonProperty
	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	@JsonProperty
	public String getZkServerAddress() {
		return zkServerAddress;
	}

	public void setZkServerAddress(String zkServerAddress) {
		this.zkServerAddress = zkServerAddress;
	} 

	

	
	
}

