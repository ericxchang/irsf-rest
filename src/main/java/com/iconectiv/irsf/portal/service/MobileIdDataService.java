package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.model.common.Country;

public interface MobileIdDataService {
	void clearCcNDC();
	String findMatchingCCNDC(String dialPattern);
	Country findMatchingCountry(String code, String iso2);
}
