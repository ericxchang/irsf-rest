package com.iconectiv.irsf.portal.model.customer;

public class PartitionSummary implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String type;
	private String dialPattern;

	public PartitionSummary(String type, String dialPattern) {
		super();
		this.type = type;
		this.dialPattern = dialPattern;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDialPattern() {
		return dialPattern;
	}

	public void setDialPattern(String dialPattern) {
		this.dialPattern = dialPattern;
	}

}
