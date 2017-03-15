package com.iconectiv.irsf.portal.core;

public enum DialPatternType {
	Prime2 ("PRIME-2"),
	Prime3 ("PRIME-3"),
	Prime4 ("PRIME-4"),
	;
	
	private String dialPattern;
	
	DialPatternType(String dialPattern) {
		this.dialPattern = dialPattern;
	}
	
	public String value() {
		return this.dialPattern;
	}
}
