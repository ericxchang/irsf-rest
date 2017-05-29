package com.iconectiv.irsf.portal.core;

public enum EventTypeDefinition {
	List_Update ("List Update"),
	Rule_Update ("Rule Update"),
	Partition_Update ("Partition Update"),
	Dataset_Update ("DateSet Update"), 
	MobileIdUpdate ("Refresh Data"), 
	Partition_Draft ("Partition Draft"), 
	Partition_Export ("Partition Export"), 
	Partition_Stale("Partition Stale"),
	Partition_PushToEI("Push to EI"),
	;
	
	private String eventType;
	
	EventTypeDefinition(String eventType) {
		this.eventType = eventType;
	}
	
	public String value() {
		return this.eventType;
	}
}
