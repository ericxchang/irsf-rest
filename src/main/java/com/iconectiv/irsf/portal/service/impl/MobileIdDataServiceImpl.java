package com.iconectiv.irsf.portal.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.iconectiv.irsf.portal.core.AppConstants;
import com.iconectiv.irsf.portal.model.common.Country;
import com.iconectiv.irsf.portal.model.common.RangeNdc;
import com.iconectiv.irsf.portal.repositories.common.CcNdcIndexRepository;
import com.iconectiv.irsf.portal.repositories.common.CountryRepository;
import com.iconectiv.irsf.portal.repositories.common.RangeNdcRepository;
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
	@Autowired
	RangeNdcRepository rangeNdcRepo;
	
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

	@Override
	public Page<RangeNdc> findRangeNdcbyFilters(List<String> codeList, 
			                                    List<String> iso2List, 
			                                    List<String> tosList,			
			                                    List<String> tosDescList, 
			                                    List<String> providerList, 
			                                    Pageable page) {
		
		Page<RangeNdc> results = null;
		int rule = 0;
		if (codeList != null && !codeList.isEmpty())
			rule += AppConstants.CODE;
		if (iso2List != null && !iso2List.isEmpty())  
			rule += AppConstants.ISO2;
		if (tosList != null && !tosList.isEmpty())
			rule += AppConstants.TOS;
		if (tosDescList != null && !tosDescList.isEmpty())
			rule += AppConstants.TOSDESC;
		if (providerList != null && !providerList.isEmpty())
			rule += AppConstants.PROVIDER;
		 
		switch(rule) {
		case 0:
			results = rangeNdcRepo.findAll(page);
			break;
		case 1:
			results = rangeNdcRepo.findRangeNdcbyRule1(codeList, page);
			break;
		case 2:
			results = rangeNdcRepo.findRangeNdcbyRule2(iso2List, page);
			break;
		case 3:
			results = rangeNdcRepo.findRangeNdcbyRule3(codeList, iso2List, page);
			break;
		case 4:
			results = rangeNdcRepo.findRangeNdcbyRule4(tosList, page);
			break;
		case 5:
			results = rangeNdcRepo.findRangeNdcbyRule5(codeList, tosList, page);
			break;
		case 6:
			results = rangeNdcRepo.findRangeNdcbyRule6(iso2List, tosList, page);
			break;
		case 7:
			results = rangeNdcRepo.findRangeNdcbyRule7(codeList, iso2List, tosList, page);
			break;
		case 8:
			results = rangeNdcRepo.findRangeNdcbyRule8(tosDescList, page);
			break;
		case 9:
			results = rangeNdcRepo.findRangeNdcbyRule9(codeList, tosDescList, page);
			break;
		case 10:
			results = rangeNdcRepo.findRangeNdcbyRule10(iso2List, tosDescList, page);
			break;
		case 11:
			results = rangeNdcRepo.findRangeNdcbyRule11(codeList, iso2List, tosDescList, page);
			break;
		case 12:
			results = rangeNdcRepo.findRangeNdcbyRule12(tosList, tosDescList, page);
			break;
		case 13:
			results = rangeNdcRepo.findRangeNdcbyRule13(codeList, tosList, tosDescList, page);
			break;
		case 14:
			results = rangeNdcRepo.findRangeNdcbyRule14(iso2List, tosList, tosDescList, page);
			break;
		case 15:
			results = rangeNdcRepo.findRangeNdcbyRule15(codeList, iso2List, tosList, tosDescList, page);
			break;
		case 16:
			results = rangeNdcRepo.findRangeNdcbyRule16(providerList, page);
			break;
		case 17:
			results = rangeNdcRepo.findRangeNdcbyRule17(codeList, providerList, page);
			break;
		case 18:
			results = rangeNdcRepo.findRangeNdcbyRule18(iso2List, providerList, page);
			break;
		case 19:
			results = rangeNdcRepo.findRangeNdcbyRule19(codeList, iso2List, providerList, page);
			break;
		case 20:
			results = rangeNdcRepo.findRangeNdcbyRule20(tosList, providerList, page);
			break;
		case 21:
			results = rangeNdcRepo.findRangeNdcbyRule21(codeList, tosList, providerList, page);
			break;
		case 22:
			results = rangeNdcRepo.findRangeNdcbyRule22(iso2List, tosList, providerList, page);
			break;
		case 23:
			results = rangeNdcRepo.findRangeNdcbyRule23(codeList, iso2List, tosList, providerList, page);
			break;
		case 24:
			results = rangeNdcRepo.findRangeNdcbyRule24(tosDescList, providerList, page);
			break;
		case 25:
			results = rangeNdcRepo.findRangeNdcbyRule25(codeList, tosDescList, providerList, page);
			break;
		case 26:
			results = rangeNdcRepo.findRangeNdcbyRule26(iso2List, tosDescList, providerList, page);
			break;
		case 27:
			results = rangeNdcRepo.findRangeNdcbyRule27(codeList, iso2List, tosDescList, providerList, page);
			break;
		case 28:
			results = rangeNdcRepo.findRangeNdcbyRule28(tosList, tosDescList, providerList, page);
			break;
		case 29:
			results = rangeNdcRepo.findRangeNdcbyRule29(codeList, tosList, tosDescList, providerList, page);
			break;
		case 30:
			results = rangeNdcRepo.findRangeNdcbyRule30(iso2List, tosList, tosDescList, providerList, page);
			break;
		case 31:
			results = rangeNdcRepo.findRangeNdcbyRule31(codeList, iso2List, tosList, tosDescList, providerList, page);
			break;
		default:
			break;

		}
		
		return results;
	}

	
}
