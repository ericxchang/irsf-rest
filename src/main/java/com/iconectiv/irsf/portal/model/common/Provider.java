package com.iconectiv.irsf.portal.model.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Provider {
	
	private String provider;
	private String billingId;
	
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
        if (!(o instanceof Provider)) {
            return false;
        }
        Provider oneProvider = (Provider) o;
        return this.provider.equals(oneProvider.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, billingId);
    }
	


}
