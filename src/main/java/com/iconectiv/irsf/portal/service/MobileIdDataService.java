package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.model.common.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface MobileIdDataService {
	void cleanCache();
	
	List<ProviderBillingId> findProviders();
	
	List<TosTosDesc> findAllTOS();
	
	List<RangeNdc> findAllTosProviderList();

	List<RangeNdc> findAllCountryProviderList();

	int getTotalTOSCount(String tos);
	
	String findProviderByBillingId(String billingId);
	
	List<String> findBillingIdsByProvider(String provider);
	
	String findMatchingCCNDC(String dialPattern);
	
	Country findMatchingCountry(String code, String iso2);
	
	Page<RangeNdc> findRangeNdcByFilters(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Pageable pageable);
	
	Page<Premium> findPremiumRangeByFilters(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

    List<RangeNdc> findAllRangeNdcByFilters(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList);
	
	List<Premium> findAllPremiumRangeByFilters(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);
	
    Page<RangeNdc> findRangeNdcByFilters(RangeQueryFilter filter);
	
	Page<Premium> findPremiumRangeByFilters(RangeQueryFilter filter);
	
	List<RangeNdc> findAllRangeNdcByFilters(RangeQueryFilter filter);
		
	List<Premium> findAllPremiumRangeByFilters(RangeQueryFilter filter);

	String getLastDataSetDate();

	Page<RangeNdc> findRangeNdcByFilters(RangeQueryFilter filter, Pageable page);

	Page<Premium> findPremiumRangeByFilters(RangeQueryFilter filter, Pageable page);

    List<ProviderBillingId> findDistinctProviders();

}

