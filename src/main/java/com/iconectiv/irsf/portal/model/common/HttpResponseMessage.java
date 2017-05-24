package com.iconectiv.irsf.portal.model.common;

import org.springframework.http.HttpStatus;

public class HttpResponseMessage {
	
    private HttpStatus httpStatus;
	private String message;
	private String status;
	private String id;
	
	
	public HttpResponseMessage(HttpStatus httpStatus, String message, String status, String id) {
		super();
		this.httpStatus = httpStatus;
		this.message = message;
		this.status = status;
		this.id = id;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
