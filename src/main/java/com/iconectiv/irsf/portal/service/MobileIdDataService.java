package com.iconectiv.irsf.portal.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.iconectiv.irsf.portal.model.common.Country;
import com.iconectiv.irsf.portal.model.common.RangeNdc;

public interface MobileIdDataService {
	void clearCcNDC();
	String findMatchingCCNDC(String dialPattern);
	Country findMatchingCountry(String code, String iso2);
	Page<RangeNdc> findRangeNdcbyFilters(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Pageable pageable);

}
