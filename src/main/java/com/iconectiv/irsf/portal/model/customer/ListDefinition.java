package com.iconectiv.irsf.portal.model.customer;
// Generated Mar 6, 2017 5:15:33 PM by Hibernate Tools 3.2.2.GA

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * ListDefintion generated by hbm2java
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "list_definition", uniqueConstraints = @UniqueConstraint(columnNames = "list_name"))
public class ListDefinition implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String customerName;
	private String listName;
	private String type;
	private String description;
	private String lastUpdatedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm Z")
	private Date lastUpdated;
	private String createBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date createTimestamp;
	private List<ListUploadRequest> listUploadRequests = new ArrayList<>();
    private boolean active;
	private String lastUploadStatus;
	private int listSize;
    
	public ListDefinition() {
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

	@Column(name = "customer_name", nullable = false, length = 45)
	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Column(name = "list_name", unique = true, nullable = false, length = 25)
	public String getListName() {
		return this.listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	@Column(name = "type", nullable = false, length = 2)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "description", length = 100)
	public String getDescription() {
		if (this.description == null) {
			this.description = "";
		}
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    @Column(name = "active", nullable = false)
    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

	@Column(name = "last_updated_by", nullable = false, length = 45)
	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_updated", nullable = false, length = 19)
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Column(name = "create_by", nullable = false, length = 45)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_timestamp", nullable = false, length = 19)
	public Date getCreateTimestamp() {
		return this.createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	@Transient
	public List<ListUploadRequest> getListUploadRequests() {
		return this.listUploadRequests;
	}

	@Transient
	public String getLastUploadStatus() {
		return lastUploadStatus;
	}

	public void setLastUploadStatus(String lastUploadStatus) {
		this.lastUploadStatus = lastUploadStatus;
	}

	public void setListUploadRequests(List<ListUploadRequest> listUploadRequests) {
		this.listUploadRequests = listUploadRequests;
		
		if (!listUploadRequests.isEmpty()) {
			this.lastUploadStatus = listUploadRequests.get(0).getStatus();
		}
	}

	@Transient
	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

}
