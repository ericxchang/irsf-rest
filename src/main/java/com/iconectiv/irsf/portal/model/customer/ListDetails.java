package com.iconectiv.irsf.portal.model.customer;
// Generated Mar 6, 2017 5:15:33 PM by Hibernate Tools 3.2.2.GA

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.iconectiv.irsf.util.DateTimeHelper;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * ListDetails generated by hbm2java
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "list_details", uniqueConstraints = @UniqueConstraint(columnNames={"list_ref_id", "dial_pattern"}))
public class ListDetails implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer listRefId;
	private Integer upLoadRefId;
	private String dialPattern;
	private String reason;
	private String notes;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date customerDate;
	private boolean active;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mmZ")
	private Date lastUpdated;
	private String lastUpdatedBy;
	private String matchCCNDC;

	// join from range_ndc table
	private String termCountry;
	private String ccNdc;
	private String iso2;
	private String code;
	private String tos;
	private String tosdesc;
	private String ndc;
	private String provider;
	private String billingId;
	private String supplement;
	private Date effectiveDate;

	public ListDetails() {
	}
	
	public ListDetails(int id, int listRefId, int upLoadRefId, String dialPattern, String reason, String notes, Date customerDate, 
			boolean active, String matchCCNDC, Date lastUpdated, String lastUpdatedBy, String termCountry, String ccNdc, String iso2, 
			String code, String tos, String tosdesc, String ndc, String provider, String billingId, String supplement,  Date effectiveDate) {
		this.id = id;
		this.listRefId = listRefId;
		this.upLoadRefId = upLoadRefId;
		this.dialPattern = dialPattern;
		this.reason = reason;
		this.notes = notes;
		this.customerDate = customerDate;
		this.active = active;
		this.matchCCNDC = matchCCNDC;
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
		this.termCountry = termCountry;
		this.ccNdc = ccNdc;
		this.iso2 = iso2;
		this.code = code;
		this.tos = tos;
		this.tosdesc = tosdesc;
		this.ndc = ndc;
		this.provider = provider;
		this.billingId = billingId;
		this.supplement = supplement;
		this.effectiveDate = effectiveDate;				
	}

	@Override
	public boolean equals(Object v) {
		if (v instanceof ListDetails) {
			ListDetails obj = (ListDetails) v;
			if (this.id == null && obj.getId() == null) {
				return this.dialPattern.equals(obj.getDialPattern());
			}
			if ((this.id == null && obj.getId() != null) || (this.id != null && obj.getId() == null)) {
				return false;
			}

			return this.id.equals(obj.getId()) && this.dialPattern.equals(obj.getDialPattern());
		}

		return false;
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

	@Column(name = "list_ref_id", nullable = false)
	public Integer getListRefId() {
		return listRefId;
	}

	public void setListRefId(Integer listRefId) {
		this.listRefId = listRefId;
	}

	@Column(name = "upload_req_ref_id")
	public Integer getUpLoadRefId() {
		return upLoadRefId;
	}

	public void setUpLoadRefId(Integer upLoadRefId) {
		this.upLoadRefId = upLoadRefId;
	}

	@Column(name = "dial_pattern", nullable = false, length = 15)
	public String getDialPattern() {
		return this.dialPattern;
	}

	public void setDialPattern(String dialPattern) {
		this.dialPattern = dialPattern;
	}


	@Column(name = "match_cc_ndc", length = 15)
	public String getMatchCCNDC() {
		return matchCCNDC;
	}

	public void setMatchCCNDC(String matchCCNDC) {
		this.matchCCNDC = matchCCNDC;
	}

	@Column(name = "reason", length = 100)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		if (reason != null) {
			reason = reason.replaceAll("\n", " ");
		}
		this.reason = reason;
	}

	@Column(name = "notes", length = 100)
	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		if (notes != null) {
			notes = notes.replaceAll("\n", " ");
		}
		this.notes = notes;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "customer_date", length = 10)
	public Date getCustomerDate() {
		return this.customerDate;
	}

	public void setCustomerDate(Date customerDate) {
		if (customerDate != null) {
			customerDate = DateTimeHelper.toUTC(customerDate);
		}
		this.customerDate = customerDate;
	}

	@Column(name = "active", nullable = false)
	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_updated", nullable = false, length = 19)
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Column(name = "last_updated_by", nullable = false, length = 45)
	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	// join from range_ndc table
	@Transient
	public String getTermCountry() {
		return termCountry;
	}

	public void setTermCountry(String termCountry) {
		this.termCountry = termCountry;
	}

	@Transient
	public String getCcNdc() {
		return ccNdc;
	}

	public void setCcNdc(String ccNdc) {
		this.ccNdc = ccNdc;
	}

	@Transient
	public String getIso2() {
		return iso2;
	}

	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}

	@Transient
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Transient
	public String getTos() {
		return tos;
	}

	public void setTos(String tos) {
		this.tos = tos;
	}

	@Transient
	public String getTosdesc() {
		return tosdesc;
	}

	public void setTosdesc(String tosdesc) {
		this.tosdesc = tosdesc;
	}

	@Transient
	public String getNdc() {
		return ndc;
	}

	public void setNdc(String ndc) {
		this.ndc = ndc;
	}

	@Transient
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	@Transient
	public String getBillingId() {
		return billingId;
	}

	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}

	@Transient
	public String getSupplement() {
		return supplement;
	}

	public void setSupplement(String supplement) {
		this.supplement = supplement;
	}

	@Transient
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

    @Transient
	public PartitionDataDetails toPartitionDataDetails(PartitionDefinition partition, ListDefinition listDef, String type) {
        PartitionDataDetails p = new PartitionDataDetails();
        p.setPartitionId(partition.getId());
        p.setBillingId(this.getBillingId());
        p.setCode(this.getCode());
        p.setCustomerDate(this.getCustomerDate());
        p.setDialPattern(this.getDialPattern());
        p.setCountry(this.getTermCountry());
        p.setIso2(this.getIso2());
        p.setNdc(this.getNdc());
        p.setNotes(this.getNotes());
        p.setProvider(this.getProvider());
        p.setReason(this.getReason());
        p.setReference(listDef.getListName());
        p.setTos(this.getTos());
        p.setTosdesc(this.getTosdesc());
        p.setDataType(listDef.getType());
        p.setType(type);

        return p;
    }


}
