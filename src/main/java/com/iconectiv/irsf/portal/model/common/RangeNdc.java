package com.iconectiv.irsf.portal.model.common;
// Generated Mar 9, 2017 1:15:40 PM by Hibernate Tools 3.2.2.GA

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * RangeNdc generated by hbm2java
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "range_ndc", catalog = "irsfmast")
public class RangeNdc implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String termCountry;
	private String ccNdc;
	private String iso2;
	private String code;
	private String tos;
	private String tosdesc;
	private String ndc;
	private String locality;
	private String provider;
	private String billingId;
	private String supplement;
	private Date effectiveDate;

	public RangeNdc() {
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

	@Column(name = "term_country", length = 40)
	public String getTermCountry() {
		return this.termCountry;
	}

	public void setTermCountry(String termCountry) {
		this.termCountry = termCountry;
	}

	@Column(name = "cc_ndc", length = 18)
	public String getCcNdc() {
		return this.ccNdc;
	}

	public void setCcNdc(String ccNdc) {
		this.ccNdc = ccNdc;
	}

	@Column(name = "iso2", length = 2)
	public String getIso2() {
		return this.iso2;
	}

	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}

	@Column(name = "code", length = 3)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "tos", length = 1)
	public String getTos() {
		return this.tos;
	}

	public void setTos(String tos) {
		this.tos = tos;
	}

	@Column(name = "tosdesc", length = 20)
	public String getTosdesc() {
		return this.tosdesc;
	}

	public void setTosdesc(String tosdesc) {
		this.tosdesc = tosdesc;
	}

	@Column(name = "ndc", length = 15)
	public String getNdc() {
		return this.ndc;
	}

	public void setNdc(String ndc) {
		this.ndc = ndc;
	}

	@Column(name = "locality", length = 50)
	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	@Column(name = "provider", length = 50)
	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	@Column(name = "billing_id", length = 10)
	public String getBillingId() {
		return this.billingId;
	}

	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}

	@Column(name = "supplement", length = 40)
	public String getSupplement() {
		return this.supplement;
	}

	public void setSupplement(String supplement) {
		this.supplement = supplement;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "effective_date", length = 10)
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
}
