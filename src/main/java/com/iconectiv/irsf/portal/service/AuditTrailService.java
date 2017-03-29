package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.model.common.AuditTrail;

import java.util.Map;

/**
 * Created by echang on 1/14/2017.
 */
public interface AuditTrailService {
    void saveAuditTrailLog(AuditTrail audit);
	void saveAuditTrailLog(AuditTrail audit, Map<String, String> auditDetail);
    void saveAuditTrailLog(String userName, String customerName, String action, String detail);
    void saveAuditTrailLog(String userName, String customerName, String action, String detail, String lastUpdatedBy);
}
