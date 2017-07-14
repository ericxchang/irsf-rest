package com.iconectiv.irsf.portal.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "provider_billing_id", catalog = "irsfmast")
public class ProviderBillingId {
	
	private Integer id;
	private String provider;
	String billingId;
	
	public ProviderBillingId() {	
	}

	public ProviderBillingId(String provider) {
		this.provider = provider;
	}

	public ProviderBillingId(String billingId, String provider) {
		this.provider = provider;
		this.billingId = billingId;
	}
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "provider", length = 50)
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	@Column(name = "billing_id", length = 10)
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
