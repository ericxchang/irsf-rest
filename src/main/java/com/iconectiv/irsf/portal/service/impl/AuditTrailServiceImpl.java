package com.iconectiv.irsf.portal.service.impl;

import com.iconectiv.irsf.portal.service.AuditTrailService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by echang on 1/14/2017.
 */
@Service
public class AuditTrailServiceImpl implements AuditTrailService{
    @Override
    @Async
    public void saveAuditTrailLog(String data) {
        //TODO add entry in audit_trail table
    }
}
