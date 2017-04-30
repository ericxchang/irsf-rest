package com.iconectiv.irsf.portal.model.common;

import java.util.Objects;

public class ProviderBillingId {
	
	private String provider;
	private String billingId;
	
	public ProviderBillingId(String billingId, String provider) {
		this.provider = provider;
		this.billingId = billingId;
	}
	
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
    public String getBillingId() {
		return billingId;
	}
	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}
	
	@Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof ProviderBillingId)) {
            return false;
        }
        ProviderBillingId instance = (ProviderBillingId) o;
        return this.billingId.equals(instance.billingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, billingId);
    }
	


}
