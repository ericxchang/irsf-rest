package com.iconectiv.irsf.portal.core;

public enum EventTypeDefinition {
	List_Update ("List Update"),
	Rule_Update ("Rule Update"),
	Partition_Update ("Partition Update"),
	Dataset_Update ("DateSet Update"), 
	MobildIdUpdate ("MobileId Update"),
	;
	
	private String eventType;
	
	EventTypeDefinition(String eventType) {
		this.eventType = eventType;
	}
	
	public String value() {
		return this.eventType;
	}
}
