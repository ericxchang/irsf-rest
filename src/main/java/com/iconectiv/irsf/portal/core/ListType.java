package com.iconectiv.irsf.portal.core;

public enum ListType {
	Black ("BL"),
	White ("WL"),
	;
	
	private String listType;
	
	ListType(String listType) {
		this.listType = listType;
	}
	
	public String value() {
		return this.listType;
	}
}
