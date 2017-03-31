package com.iconectiv.irsf.portal.core;

public enum PartitionStatus {
	Fresh ("fresh"),
	Draft ("draft"),
	Locked ("locked"),
	Stale ("stale"),
	Processing("processing"),
	;
	
	private String value;
	
	PartitionStatus(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
}
