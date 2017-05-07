package com.iconectiv.irsf.portal.model.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TosAndTosDescType {
	private String tos;
	private List<String> tosDescs;
	
	public List<String> getTosDescs() {
		return tosDescs;
	}
	public void setTosDescs(List<String> tosDescs) {
		this.tosDescs = tosDescs;
	}
	public String getTos() {
		return tos;
	}
	public void setTos(String tos) {
		this.tos = tos;
	}
	
	
	
	
}
