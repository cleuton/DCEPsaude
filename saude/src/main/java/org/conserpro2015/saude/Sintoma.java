package org.conserpro2015.saude;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Sintoma {
	private long regiao;
	private int sintoma;
	private String data;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public Sintoma(long regiao,int sintoma,String data) {
		this();
		this.regiao = regiao;
		this.sintoma = sintoma;
		this.data = data;
	}
	
	public Sintoma() {
		super();
	}
	
	@JsonProperty
	public long getRegiao() {
		return regiao;
	}
	public void setRegiao(long regiao) {
		this.regiao = regiao;
	}
	
	@JsonProperty
	public int getSintoma() {
		return sintoma;
	}
	public void setSintoma(int sintoma) {
		this.sintoma = sintoma;
	}
	
	@JsonProperty
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writer().writeValueAsString(this);
		}
		catch(Exception ex) {
			logger.error("Erro ao serializar sintoma: " + ex.getMessage());
			return null;
		}
	}
	
	
}
