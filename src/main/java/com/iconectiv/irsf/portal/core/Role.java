package com.iconectiv.irsf.portal.core;

public enum Role {
	Admin ("admin"),
	API ("api"),
	CustAdmin ("custAdmin"),
	User ("user"),
	;
	
	private String role;
	
	Role(String role) {
		this.role = role;
	}
	
	public String value() {
		return this.role;
	}
}
