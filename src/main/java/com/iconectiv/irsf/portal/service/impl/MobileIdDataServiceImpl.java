package com.iconectiv.irsf.portal.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.iconectiv.irsf.portal.repositories.common.CcNdcIndexRepository;
import com.iconectiv.irsf.portal.service.MobileIdDataService;

@Service
public class MobileIdDataServiceImpl implements MobileIdDataService {
	private static Logger log = LoggerFactory.getLogger(MobileIdDataServiceImpl.class);
	
	Set<String> ccNdcData = new HashSet<>();
	
	@Autowired
	CcNdcIndexRepository ccNdcRepo;
	
	@CacheEvict(value = "ccNDC", allEntries = true)
	@Override
	public void clearCcNDC() {
		log.info("Clear ccNDC data from cache");
	}

	@Override
	public String findMatchingCCNDC(String dialPattern) {
		if (dialPattern == null || dialPattern.length()<1) {
			return dialPattern;
		}
		if (ccNdcData.size() < 1) {
			ccNdcData = ccNdcRepo.findAllItem();
		}
		
		for (int i=dialPattern.length(); i>0; i--) {
			String value = dialPattern.substring(0, i-1);
			if (ccNdcData.contains(value)) {
				return value;
			}
		}
		return dialPattern;
	}

}
