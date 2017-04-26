package com.iconectiv.irsf.portal.core;

import java.util.List;

public class ProviderBillingId {
	
	private String provider;
	private List<String> billingIds;
	
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public List<String> getBillingIds() {
		return billingIds;
	}
	public void setBillingIds(List<String> billingIds) {
		this.billingIds = billingIds;
	}


}
