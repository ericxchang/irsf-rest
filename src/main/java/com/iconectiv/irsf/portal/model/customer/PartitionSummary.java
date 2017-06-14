package com.iconectiv.irsf.portal.model.customer;

public class PartitionSummary implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String dialPattern;
	private String type;

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
	public String toString() {
		return toCSVString(",");
	}
	public String toCSVheader(String seperator) {
		 StringBuffer sb = new StringBuffer();
		 sb.append("dialPattern");
		 sb.append(seperator);
		 sb.append("type");
		
		 return sb.toString();
	}

	public String toCSVString(String seperator) {
		 StringBuffer sb = new StringBuffer();
		 sb.append(dialPattern);
		 sb.append(seperator);
		 sb.append(type);
		 
		 return sb.toString();
	}


}
