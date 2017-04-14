package com.iconectiv.irsf.portal.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.iconectiv.irsf.portal.model.common.Country;
import com.iconectiv.irsf.portal.repositories.common.CcNdcIndexRepository;
import com.iconectiv.irsf.portal.repositories.common.CountryRepository;
import com.iconectiv.irsf.portal.service.MobileIdDataService;

@Service
public class MobileIdDataServiceImpl implements MobileIdDataService {
	private static Logger log = LoggerFactory.getLogger(MobileIdDataServiceImpl.class);
	
	Set<String> ccNdcData = new HashSet<>();
	List<Country> countryList = new ArrayList<>();
	
	@Autowired
	CcNdcIndexRepository ccNdcRepo;
	@Autowired
	CountryRepository countryRepo;
	
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
		if (ccNdcData.isEmpty()) {
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

	@Override
	public Country findMatchingCountry(String code, String iso2) {
		if (countryList.isEmpty()) {
			countryList = countryRepo.findAll();
		}
		
		
		for (Country country: countryList) {
			if (country.getCode().equals(code) && country.getIso2().equals(iso2)) {
				return country;
			}
		}
		return null;
	}

}
