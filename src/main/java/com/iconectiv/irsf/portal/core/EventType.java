package com.iconectiv.irsf.portal.core;

public enum EventType {
	List_Update ("List_Update"),
	Rule_Update ("Rule_Update"),
	Partition_Update ("Partition_Update"),
	Dataset_Update ("DateSet_Update"),
	;
	
	private String eventType;
	
	EventType(String eventType) {
		this.eventType = eventType;
	}
	
	public String value() {
		return this.eventType;
	}
}
