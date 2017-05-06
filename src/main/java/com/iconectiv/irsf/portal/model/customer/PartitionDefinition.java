package com.iconectiv.irsf.portal.model.customer;
// Generated Mar 9, 2017 1:11:16 PM by Hibernate Tools 3.2.2.GA

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * PartitionDefintion generated by hbm2java
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

@Entity
@Table(name = "partition_definition")
public class PartitionDefinition implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
    private Integer origPartitionId;
	private String customerName;
	private String name;
	private String description;
	private Integer wlId;
	private Integer blId;
	private String ruleIds;
	private String status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date draftDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date lastExportDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date lastUpdated;
	private String lastUpdatedBy;

	private List<PartitionExportHistory> partitionExportHistories = new ArrayList<>();
	private List<PartitionDataDetails> partitionDataDetailses = new ArrayList<>();
	private List<RuleDefinition> ruleDefinitions = new ArrayList<>();

	public PartitionDefinition() {
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

    @Column(name = "orig_partition_id")
    public Integer getOrigPartitionId() {
		return origPartitionId;
	}


	public void setOrigPartitionId(Integer origPartitionId) {
		this.origPartitionId = origPartitionId;
	}


	@Column(name = "customer_name", nullable = false, length=45)
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Column(name = "name", nullable = false, length = 15)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 100)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "wl_id")
	public Integer getWlId() {
		return this.wlId;
	}

	public void setWlId(Integer wlId) {
		this.wlId = wlId;
	}

	@Column(name = "bl_id")
	public Integer getBlId() {
		return this.blId;
	}

	public void setBlId(Integer blId) {
		this.blId = blId;
	}

	@Column(name = "rule_ids", length = 250)
	public String getRuleIds() {
		return this.ruleIds;
	}

	public void setRuleIds(String ruleIds) {
		this.ruleIds = ruleIds;
	}

	@Column(name = "status", length = 15)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "draft_date", length = 19)
	public Date getDraftDate() {
		return this.draftDate;
	}

	public void setDraftDate(Date draftDate) {
		this.draftDate = draftDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_export_date", length = 19)
	public Date getLastExportDate() {
		return this.lastExportDate;
	}

	public void setLastExportDate(Date lastExportDate) {
		this.lastExportDate = lastExportDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_updated", length = 19)
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Column(name = "last_updated_by", length = 45)
	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	@Transient
	public List<PartitionExportHistory> getPartitionExportHistories() {
		return this.partitionExportHistories;
	}

	public void setPartitionExportHistories(List<PartitionExportHistory> partitionExportHistories) {
		this.partitionExportHistories = partitionExportHistories;
	}

	@Transient
	public List<PartitionDataDetails> getPartitionDataDetailses() {
		return this.partitionDataDetailses;
	}

	public void setPartitionDataDetailses(List<PartitionDataDetails> partitionDataDetailses) {
		this.partitionDataDetailses = partitionDataDetailses;
	}

	@Transient
	public List<RuleDefinition> getRuleDefinitions() {
		return this.ruleDefinitions;
	}

	public void setRuleDefinitions(List<RuleDefinition> ruleDefinitions) {
		this.ruleDefinitions = ruleDefinitions;
	}

	public void addRule(RuleDefinition rule) {
		this.ruleDefinitions.add(rule);
	}
}
