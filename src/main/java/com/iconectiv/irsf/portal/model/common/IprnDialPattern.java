package com.iconectiv.irsf.portal.model.common;
// Generated Mar 22, 2017 11:07:03 AM by Hibernate Tools 3.2.2.GA

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * IprnDialPatternVw generated by hbm2java
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "iprn_dial_pattern")
public class IprnDialPattern implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String termCountry;
	private String dialPattern;
	private String dialPatternType;
	private String iso2;
	private String code;
	private String tos;
	private String tosdesc;
	private String ndc;
	private String locality;
	private String provider;
	private String supplement;
	private Date lastUpdate;

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	@Column(name = "TERM_COUNTRY", length = 40)
	public String getTermCountry() {
		return this.termCountry;
	}

	public void setTermCountry(String termCountry) {
		this.termCountry = termCountry;
	}

	@Column(name = "DIAL_PATTERN", length = 15)
	public String getDialPattern() {
		return this.dialPattern;
	}

	public void setDialPattern(String dialPattern) {
		this.dialPattern = dialPattern;
	}

	@Column(name = "DIAL_PATTERN_TYPE", nullable = false, length = 7)
	public String getDialPatternType() {
		return this.dialPatternType;
	}

	public void setDialPatternType(String dialPatternType) {
		this.dialPatternType = dialPatternType;
	}

	@Column(name = "ISO2", length = 2)
	public String getIso2() {
		return this.iso2;
	}

	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}

	@Column(name = "CODE", length = 3)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "TOS", length = 1)
	public String getTos() {
		return this.tos;
	}

	public void setTos(String tos) {
		this.tos = tos;
	}

	@Column(name = "TOSDESC", length = 20)
	public String getTosdesc() {
		return this.tosdesc;
	}

	public void setTosdesc(String tosdesc) {
		this.tosdesc = tosdesc;
	}

	@Column(name = "NDC", length = 15)
	public String getNdc() {
		return this.ndc;
	}

	public void setNdc(String ndc) {
		this.ndc = ndc;
	}

	@Column(name = "LOCALITY", length = 50)
	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	@Column(name = "PROVIDER", length = 50)
	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	@Column(name = "SUPPLEMENT", length = 40)
	public String getSupplement() {
		return this.supplement;
	}

	public void setSupplement(String supplement) {
		this.supplement = supplement;
	}

	@Column(name = "LAST_UPDATE", length = 10)
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
