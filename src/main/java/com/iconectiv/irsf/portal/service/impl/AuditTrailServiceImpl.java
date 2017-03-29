package com.iconectiv.irsf.portal.service.impl;

import com.google.common.base.Joiner;
import com.iconectiv.irsf.portal.model.common.AuditTrail;
import com.iconectiv.irsf.portal.repositories.common.AuditTrailRepository;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * Created by echang on 1/14/2017.
 */
@Service
public class AuditTrailServiceImpl implements AuditTrailService{
	private static Logger log = LoggerFactory.getLogger(AuditTrailServiceImpl.class);
	
	@Autowired
	private AuditTrailRepository auditRepo;
	
    @Transactional
	@Override    
    public void saveAuditTrailLog(AuditTrail audit) {
        audit.setLastUpdated(new Date());        
        audit.setLastUpdatedBy(audit.getUserName());        
        auditRepo.save(audit);
        log.info("Save audit trail record: \n" + JsonHelper.toJson(audit));

    }

	@Override
	public void saveAuditTrailLog(String userName, String customerName, String action, String details) {
		AuditTrail audit = new AuditTrail();
		audit.setUserName(userName);
		audit.setCustomerName(customerName);
		audit.setAction(action);
		audit.setDetails(details);
		
		saveAuditTrailLog(audit);
	}

	@Override
	public void saveAuditTrailLog(String userName, String customerName, String action, String details, String lastUpdatedBy) {
		AuditTrail audit = new AuditTrail();
		audit.setUserName(userName);
		audit.setCustomerName(customerName);
		audit.setAction(action);
		audit.setDetails(details);
		audit.setLastUpdatedBy(lastUpdatedBy);
		auditRepo.save(audit);
		log.info("Save audit trail record: \n" + JsonHelper.toJson(audit));
	}

	@Override
	public void saveAuditTrailLog(AuditTrail audit, Map<String, String> auditDetail) {
		audit.setDetails(Joiner.on(",").withKeyValueSeparator("=").join(auditDetail));
		saveAuditTrailLog(audit);
	}
}
