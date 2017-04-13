package com.iconectiv.irsf.portal.service;

public interface MobileIdDataService {
	void clearCcNDC();
	String findMatchingCCNDC(String dialPattern);
}
