package com.iconectiv.irsf.portal.core;

public enum PermissionRole {
	Admin ("admin"),
	API ("api"),
	CustAdmin ("custAdmin"),
	User ("user"),
	;
	
	private String role;
	
	PermissionRole(String role) {
		this.role = role;
	}
	
	public String value() {
		return this.role;
	}
}
