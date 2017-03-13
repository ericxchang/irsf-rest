package com.iconectiv.irsf.portal.model.common;
// Generated Mar 9, 2017 1:15:40 PM by Hibernate Tools 3.2.2.GA

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Premium generated by hbm2java
 */
@Entity
@Table(name = "premium")
public class Premium implements java.io.Serializable {
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
	private String jobId;
	private Date loadDate;

	public Premium() {
	}

	public Premium(String dialPatternType, Date loadDate) {
		this.dialPatternType = dialPatternType;
		this.loadDate = loadDate;
	}

	public Premium(String termCountry, String dialPattern, String dialPatternType, String iso2, String code, String tos,
	        String tosdesc, String ndc, String locality, String provider, String supplement, Date lastUpdate,
	        String jobId, Date loadDate) {
		this.termCountry = termCountry;
		this.dialPattern = dialPattern;
		this.dialPatternType = dialPatternType;
		this.iso2 = iso2;
		this.code = code;
		this.tos = tos;
		this.tosdesc = tosdesc;
		this.ndc = ndc;
		this.locality = locality;
		this.provider = provider;
		this.supplement = supplement;
		this.lastUpdate = lastUpdate;
		this.jobId = jobId;
		this.loadDate = loadDate;
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

	@Column(name = "dial_pattern", length = 15)
	public String getDialPattern() {
		return this.dialPattern;
	}

	public void setDialPattern(String dialPattern) {
		this.dialPattern = dialPattern;
	}

	@Column(name = "dial_pattern_type", nullable = false, length = 7)
	public String getDialPatternType() {
		return this.dialPatternType;
	}

	public void setDialPatternType(String dialPatternType) {
		this.dialPatternType = dialPatternType;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "last_update", length = 10)
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Column(name = "job_id")
	public String getJobId() {
		return this.jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "load_date", nullable = false, length = 19)
	public Date getLoadDate() {
		return this.loadDate;
	}

	public void setLoadDate(Date loadDate) {
		this.loadDate = loadDate;
	}

}
