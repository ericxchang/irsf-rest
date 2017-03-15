package com.iconectiv.irsf.portal.core;

public enum PartitionStatus {
	Fresh ("fresh"),
	Draft ("draft"),
	Locked ("locked"),
	;
	
	private String value;
	
	PartitionStatus(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
}
