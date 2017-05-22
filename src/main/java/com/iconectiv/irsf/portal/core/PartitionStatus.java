package com.iconectiv.irsf.portal.core;

public enum PartitionStatus {
	Fresh ("fresh"),
	Draft ("draft"),
	Exported ("exported"),
	Stale ("stale"),
	InProgress("in-progress"),
	;
	
	private String value;
	
	PartitionStatus(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
}
