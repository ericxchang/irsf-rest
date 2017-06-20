package com.iconectiv.irsf.portal.core;

public enum PartitionStatus {
	Fresh ("Fresh"),
	Draft ("Draft"),
	Exported ("Exported"),
	Stale ("Stale"),
	InProgress("Processing"),
	;
	
	private String value;
	
	PartitionStatus(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
}
