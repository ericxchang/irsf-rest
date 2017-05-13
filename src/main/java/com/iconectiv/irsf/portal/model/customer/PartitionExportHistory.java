package com.iconectiv.irsf.portal.model.customer;
// Generated Mar 9, 2017 1:11:16 PM by Hibernate Tools 3.2.2.GA


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * PartitionExportHistory generated by hbm2java
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

@Entity
@Table(name = "partition_export_history")
public class PartitionExportHistory implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
    private Integer partitionId;
    private Integer origPartitionId;
    private byte[] exportFileLong;
    private byte[] exportFileShort;
    private byte[] exportWhitelist;
    private Integer exportFileLongSize;
    private Integer exportFileShortSize;
    private Integer exportWhitelistSize;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date exportDate;
    private Date midDataLoadTime;
    private String status;
    private String reason;
    private PartitionDefinition partitionDefinition;

    public PartitionExportHistory() {
    }


    public PartitionExportHistory(int id, int origPartitionId, Date exportDate, String status, String reason) {
        this.id = id;
        this.origPartitionId = origPartitionId;
        this.exportDate = exportDate;
        this.status = status;
        this.reason = reason;
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


    @Column(name = "orig_partition_id", nullable = false)
    public Integer getOrigPartitionId() {
		return origPartitionId;
	}


	public void setOrigPartitionId(Integer origPartitionId) {
		this.origPartitionId = origPartitionId;
	}


	@Transient
    public PartitionDefinition getPartitionDefinition() {
        return this.partitionDefinition;
    }

    public void setPartitionDefinition(PartitionDefinition partitionDefinition) {
        this.partitionDefinition = partitionDefinition;
    }

    @Column(name = "export_file_long", nullable = false)
    public byte[] getExportFileLong() {
        return this.exportFileLong;
    }

    public void setExportFileLong(byte[] exportFileLong) {
        this.exportFileLong = exportFileLong;
    }

    @Column(name = "export_file_short", nullable = false)
    public byte[] getExportFileShort() {
        return this.exportFileShort;
    }

    public void setExportFileShort(byte[] exportFileShort) {
        this.exportFileShort = exportFileShort;
    }

    @Column(name = "export_whitelist")
    public byte[] getExportWhitelist() {
        return this.exportWhitelist;
    }

    public void setExportWhitelist(byte[] exportWhitelist) {
        this.exportWhitelist = exportWhitelist;
    }
    
    @Column(name = "export_file_long_size")
    public Integer getExportFileLongSize() {
		return exportFileLongSize;
	}
    
	public void setExportFileLongSize(Integer exportFileLongSize) {
		this.exportFileLongSize = exportFileLongSize;
	}
	
	@Column(name = "export_file_short_size")
	public Integer getExportFileShortSize() {
		return exportFileShortSize;
	}
	
	public void setExportFileShortSize(Integer exportFileShortSize) {
		this.exportFileShortSize = exportFileShortSize;
	}
	
	@Column(name = "export_whitelist_size")
	public Integer getExportWhitelistSize() {
		return exportWhitelistSize;
	}
	
	public void setExportWhitelistSize(Integer exportWhitelistSize) {
		this.exportWhitelistSize = exportWhitelistSize;
	}

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "export_date", nullable = false, length = 19)
    public Date getExportDate() {
        return this.exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "mid_data_load_time", length = 19)
    public Date getMidDataLoadTime() {
		return midDataLoadTime;
	}

	public void setMidDataLoadTime(Date midDataLoadTime) {
		this.midDataLoadTime = midDataLoadTime;
	}

	@Column(name = "status", nullable = false, length = 45)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "reason", length = 1000)
    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


}


