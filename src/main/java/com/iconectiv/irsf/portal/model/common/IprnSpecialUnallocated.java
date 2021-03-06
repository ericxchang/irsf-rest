package com.iconectiv.irsf.portal.model.common;
// Generated Mar 9, 2017 1:15:40 PM by Hibernate Tools 3.2.2.GA

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * IprnSpecialUnallocated generated by hbm2java
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "iprn_special_unallocated", catalog = "irsfmast")
public class IprnSpecialUnallocated implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String termCountry;
	private String primeMinus2;
	private String primeMinus3;
	private String primeMinus4;
	private String iso2;
	private String code;
	private String tos;
	private String tosdesc;
	private String ndc;
	private String locality;
	private String provider;
	private String supplement;
	private Integer lemin;
	private Integer lemax;
	private String iprn;
	private String lastUpdate;
	private String filler;

	public IprnSpecialUnallocated() {
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

	@Column(name = "prime_minus_2", length = 15)
	public String getPrimeMinus2() {
		return this.primeMinus2;
	}

	public void setPrimeMinus2(String primeMinus2) {
		this.primeMinus2 = primeMinus2;
	}

	@Column(name = "prime_minus_3", length = 15)
	public String getPrimeMinus3() {
		return this.primeMinus3;
	}

	public void setPrimeMinus3(String primeMinus3) {
		this.primeMinus3 = primeMinus3;
	}

	@Column(name = "prime_minus_4", length = 15)
	public String getPrimeMinus4() {
		return this.primeMinus4;
	}

	public void setPrimeMinus4(String primeMinus4) {
		this.primeMinus4 = primeMinus4;
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

	@Column(name = "supplement", length = 40)
	public String getSupplement() {
		return this.supplement;
	}

	public void setSupplement(String supplement) {
		this.supplement = supplement;
	}

	@Column(name = "lemin")
	public Integer getLemin() {
		return this.lemin;
	}

	public void setLemin(Integer lemin) {
		this.lemin = lemin;
	}

	@Column(name = "lemax")
	public Integer getLemax() {
		return this.lemax;
	}

	public void setLemax(Integer lemax) {
		this.lemax = lemax;
	}

	@Column(name = "iprn", length = 2)
	public String getIprn() {
		return this.iprn;
	}

	public void setIprn(String iprn) {
		this.iprn = iprn;
	}

	@Column(name = "last_update", length = 6)
	public String getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Column(name = "filler", length = 12)
	public String getFiller() {
		return this.filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

}
