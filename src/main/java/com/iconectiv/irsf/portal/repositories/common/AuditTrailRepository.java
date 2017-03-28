package com.iconectiv.irsf.portal.repositories.common;

import org.springframework.data.repository.CrudRepository;

import com.iconectiv.irsf.portal.model.common.AuditTrail;

/**
 * Created by echang on 1/12/2017.
 */
public interface AuditTrailRepository extends CrudRepository<AuditTrail, Integer>{
}
