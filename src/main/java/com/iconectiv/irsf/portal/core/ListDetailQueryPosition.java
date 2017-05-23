package com.iconectiv.irsf.portal.core;

public enum ListDetailQueryPosition {
	id(0), 
	listRefId(1), 
	uploadReqRefId(2), 
	dialPattern(3), 
	reason(4), 
	notes(5), 
	customerDate(6), 
    active(7), 
    matchCCNdc(8), 
    lastUpdated(9), 
    lastUpdatedBy(10),
	termCountry(11), 
	ccNdc(12), 
	iso2(13), 
	code(14), 
	tos(15), 
	tosdesc(16), 
	ndc(17), 
	provider(18), 
	billingId(19),
	supplement(20), 
	effectiveDate(21);
	
	private int value;
	
	ListDetailQueryPosition(int value) {
		this.value = value;
	}
	
	public int value() {
		return this.value;
	}
}
