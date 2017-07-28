package com.iconectiv.irsf.portal.model.common;
// Generated Mar 9, 2017 1:15:40 PM by Hibernate Tools 3.2.2.GA

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.iconectiv.irsf.portal.core.AppConstants;
import com.iconectiv.irsf.portal.core.PartitionDataType;
import com.iconectiv.irsf.portal.model.customer.PartitionDataDetails;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Premium generated by hbm2java
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "premium")
public class Premium implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String termCountry;
	private String primeMinus2;
	private String primeMinus3;
	private String primeMinus4;
	private String dialPattern;
	private String dialPatternType;
	private String iso2;
	private String code;
	private String tos;
	private String tosdesc;
	private String ndc;
	private String locality;
	private String provider;
	private String billingId;
	private String supplement;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMM")
	private Date lastUpdate;

	public Premium() {
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
		return primeMinus2;
	}


	public void setPrimeMinus2(String primeMinus2) {
		this.primeMinus2 = primeMinus2;
	}

	@Column(name = "prime_minus_3", length = 15)
	public String getPrimeMinus3() {
		return primeMinus3;
	}


	public void setPrimeMinus3(String primeMinus3) {
		this.primeMinus3 = primeMinus3;
	}

	@Column(name = "prime_minus_4", length = 15)
	public String getPrimeMinus4() {
		return primeMinus4;
	}


	public void setPrimeMinus4(String primeMinus4) {
		this.primeMinus4 = primeMinus4;
	}


	@Transient
	public String getDialPattern() {
		return this.dialPattern;
	}

	public void setDialPattern(String dialPattern) {
		this.dialPattern = dialPattern;
	}

	@Transient
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

    @Column(name="billing_id", length=10)
    public String getBillingId() {
        return this.billingId;
    }
    
    public void setBillingId(String billingId) {
        this.billingId = billingId;
    }

	@Transient
	public PartitionDataDetails toPartitionDataDetails(PartitionDefinition partition, RuleDefinition rule) {
		PartitionDataDetails p = new PartitionDataDetails();
		p.setPartitionId(partition.getId());
		p.setBillingId(this.getBillingId());
		p.setCc(this.getCode());
		p.setCustomerDate(this.getLastUpdate());

		if (AppConstants.PRIME2.equals(rule.getDialPatternType())) {
			p.setDialPattern(this.getPrimeMinus2());
			p.setDialPatternType(AppConstants.EXPORT_PRIME2);
		} else if (AppConstants.PRIME3.equals(rule.getDialPatternType())) {
			p.setDialPattern(this.getPrimeMinus3());
			p.setDialPatternType(AppConstants.EXPORT_PRIME3);
			if (p.getDialPattern() == null) {
				p.setDialPattern(this.getPrimeMinus2());
				p.setDialPatternType(AppConstants.EXPORT_PRIME2);
			}
		} else if (AppConstants.PRIME4.equals(rule.getDialPatternType())) {
			p.setDialPattern(this.getPrimeMinus3());
			p.setDialPatternType(AppConstants.EXPORT_PRIME3);
			if (p.getDialPattern() == null)
				p.setDialPattern(this.getPrimeMinus3());
			if (p.getDialPattern() == null) {
				p.setDialPattern(this.getPrimeMinus2());
				p.setDialPatternType(AppConstants.EXPORT_PRIME3);
			}
		}

		p.setIso2(this.getIso2());
		p.setNdc(this.getNdc());
		p.setNotes(null);
		p.setProvider(this.getProvider());
		p.setReason(null);
		p.setReference(rule.getId().toString());
		p.setTos(this.getTos());
		p.setTosdesc(this.getTosdesc());
		p.setDataType(PartitionDataType.Rule.value());
		p.setType(AppConstants.EXPORT_TYPE_IPRN);
		p.setCountry(this.getTermCountry());

		return p;	
	}


}
