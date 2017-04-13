package com.iconectiv.irsf.portal.repositories.common;

import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;

import com.iconectiv.irsf.portal.model.common.CcNdcIndex;
import com.iconectiv.irsf.portal.repositories.ReadOnlyRepository;

/**
 * Created by echang on 1/12/2017.
 */
public interface CcNdcIndexRepository extends ReadOnlyRepository <CcNdcIndex, String>{
	@Cacheable("ccNDC")
	@Query("select cc.ccNdc from CcNdcIndex cc")
	Set<String> findAllItem();
}
