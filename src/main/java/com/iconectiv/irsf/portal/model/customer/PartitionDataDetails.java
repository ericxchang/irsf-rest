package com.iconectiv.irsf.portal.model.customer;
// Generated Mar 9, 2017 1:11:16 PM by Hibernate Tools 3.2.2.GA


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * PartitionDataDetails generated by hbm2java
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

@Entity
@Table(name = "partition_data_details")
public class PartitionDataDetails implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
    private Integer partitionId;
    private PartitionDefinition partitionDefinition;
    private String reference;    //save list name instead of id; save rule id
    private String dataType;
    private String dialPattern;
    private String type;
    private String dialPatternType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date customerDate;
    private String reason;
    private String notes;
    private String cc;
    private String ndc;
    private String iso2;
    private String tos;
    private String tosdesc;
    private String provider;
    private String billingId;

    public PartitionDataDetails() {
    }


    public PartitionDataDetails(PartitionDefinition partitionDefinition, String reference, String dialPattern) {
        this.partitionDefinition = partitionDefinition;
        this.reference = reference;
        this.dialPattern = dialPattern;
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

    @Column(name = "partition_id", nullable = false)
    public Integer getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(Integer partitionId) {
        this.partitionId = partitionId;
    }

    @Transient
    public PartitionDefinition getPartitionDefinition() {
        return this.partitionDefinition;
    }

    public void setPartitionDefinition(PartitionDefinition partitionDefinition) {
        this.partitionDefinition = partitionDefinition;
    }

    @Column(name = "reference", nullable = false, length = 45)
    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Column(name = "dial_pattern", nullable = false, length = 15)
    public String getDialPattern() {
        return this.dialPattern;
    }

    public void setDialPattern(String dialPattern) {
        this.dialPattern = dialPattern;
    }

    @Column(name="type", length=2)
    public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "dial_pattern_type", length = 10)
	public String getDialPatternType() {
		return dialPatternType;
	}


	public void setDialPatternType(String dialPatternType) {
		this.dialPatternType = dialPatternType;
	}


	@Temporal(TemporalType.DATE)
    @Column(name = "customer_date", length = 10)
    public Date getCustomerDate() {
        return this.customerDate;
    }

    public void setCustomerDate(Date customerDate) {
        this.customerDate = customerDate;
    }

    @Column(name = "reason", length = 100)
    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Column(name="data_type", length=2)
    public String getDataType() {
        return this.dataType;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
	@Column(name = "notes", length = 100)
    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Column(name = "cc", length = 3)
    public String getCc() {
        return this.cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    @Column(name = "ndc", length = 15)
    public String getNdc() {
        return this.ndc;
    }

    public void setNdc(String ndc) {
        this.ndc = ndc;
    }

    @Column(name = "iso2", length = 2)
    public String getIso2() {
        return this.iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
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

    @Column(name = "provider", length = 50)
    public String getProvider() {
        return this.provider;
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

	public String toCSVheader(String seperator) {
		 StringBuffer sb = new StringBuffer();
		 sb.append("id");
		 sb.append(seperator);
		 sb.append("partitionId");
		 sb.append(seperator);
		 sb.append("reference");
		 sb.append(seperator);
		 sb.append("dataType");
		 sb.append(seperator);
		 sb.append("dialPattern");
		 sb.append(seperator);
		 sb.append("customerDate");
		 sb.append(seperator);
		 sb.append("reason");
		 sb.append(seperator);
		 sb.append("notes");
		 sb.append(seperator);
		 sb.append("cc");
		 sb.append(seperator);
		 sb.append("ndc");
		 sb.append(seperator);
		 sb.append("iso2");
		 sb.append(seperator);
		 sb.append("tos");
		 sb.append(seperator);
		 sb.append("tosdesc");
		 sb.append(seperator);
		 sb.append("provider");
		 sb.append(seperator);
		 sb.append("billingId");
		 
		 return sb.toString();
	}

	public String toCSVString(String seperator) {
		 StringBuffer sb = new StringBuffer();
		 sb.append(id);
		 sb.append(seperator);
		 sb.append(partitionId);
		 sb.append(seperator);
		 sb.append(reference==null?"":reference);
		 sb.append(seperator);
		 sb.append(dataType==null?"":dataType);
		 sb.append(seperator);
		 sb.append(dialPattern==null?"":dialPattern);
		 sb.append(seperator);
		 sb.append(customerDate==null?"":customerDate);
		 sb.append(seperator);
		 sb.append(reason==null?"":reason);
		 sb.append(seperator);
		 sb.append(notes==null?"":notes);
		 sb.append(seperator);
		 sb.append(cc==null?"":cc);
		 sb.append(seperator);
		 sb.append(ndc==null?"":ndc);
		 sb.append(seperator);
		 sb.append(iso2==null?"":iso2);
		 sb.append(seperator);
		 sb.append(tos==null?"":tos);
		 sb.append(seperator);
		 sb.append(tosdesc==null?"":tosdesc);
		 sb.append(seperator);
		 sb.append(provider==null?"":provider);
		 sb.append(seperator);
		 sb.append(billingId==null?"":billingId);
		 
		 return sb.toString();
	}

}


