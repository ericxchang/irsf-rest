package com.iconectiv.irsf.portal.model.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Provider {
	
	private String provider;
	private List<String> billingIds =  new ArrayList<>();
	
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
	public void addBillingId(String billingId) {
		this.billingIds.add(billingId);
	}
		
	
    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Provider)) {
            return false;
        }
        Provider oneProvider = (Provider) o;
        return this.provider.equals(oneProvider.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, billingIds);
    }
	


}
