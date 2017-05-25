package com.iconectiv.irsf.portal.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.iconectiv.irsf.portal.core.AppConstants;
import com.iconectiv.irsf.portal.core.EventTypeDefinition;
import com.iconectiv.irsf.portal.model.common.Country;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.model.common.Premium;
import com.iconectiv.irsf.portal.model.common.Provider;
import com.iconectiv.irsf.portal.model.common.ProviderBillingId;
import com.iconectiv.irsf.portal.model.common.RangeNdc;
import com.iconectiv.irsf.portal.model.common.RangeQueryFilter;
import com.iconectiv.irsf.portal.model.common.TosTosDesc;
import com.iconectiv.irsf.portal.model.common.TosAndTosDescType;
import com.iconectiv.irsf.portal.repositories.common.CcNdcIndexRepository;
import com.iconectiv.irsf.portal.repositories.common.CountryRepository;
import com.iconectiv.irsf.portal.repositories.common.EventNotificationRepository;
import com.iconectiv.irsf.portal.repositories.common.PremiumRepository;
import com.iconectiv.irsf.portal.repositories.common.RangeNdcRepository;
import com.iconectiv.irsf.portal.service.MobileIdDataService;
import com.iconectiv.irsf.util.DateTimeHelper;
import com.iconectiv.irsf.util.JsonHelper;
import com.iconectiv.irsf.util.ListHelper;

@Service
public class MobileIdDataServiceImpl implements MobileIdDataService {
	private static Logger log = LoggerFactory.getLogger(MobileIdDataServiceImpl.class);

	Set<String> ccNdcData = new HashSet<String>();
	List<Country> countryList = new ArrayList<Country>();
	List<TosTosDesc> tosTosDescList = new ArrayList<TosTosDesc>();
	
	Map<String, String> billingIdProviderMap = new HashMap<>();

	@Value("${jdbc.query_batch_size:100000}")
	private int batchSize;
	
	@Autowired
	CcNdcIndexRepository ccNdcRepo;
	@Autowired
	CountryRepository countryRepo;
	@Autowired
	RangeNdcRepository rangeNdcRepo;

	@Autowired
	PremiumRepository premiumRepo;
	
	@Autowired
	EventNotificationRepository eventRepo;

	@Override
	public String findMatchingCCNDC(String dialPattern) {
		if (dialPattern == null || dialPattern.length() < 1) {
			return dialPattern;
		}
		if (ccNdcData.isEmpty()) {
			ccNdcData = ccNdcRepo.findAllItem();
		}

		for (int i = dialPattern.length(); i > 0; i--) {
			String value = dialPattern.substring(0, i - 1);
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

		for (Country country : countryList) {
			if (country.getCode().equals(code) && country.getIso2().equals(iso2)) {
				return country;
			}
		}
		return null;
	}

	@Override
	public Page<Premium> findPremiumRangeByFilters(List<String> codeList, List<String> iso2List, List<String> tosList,
	        List<String> tosDescList, List<String> providerList, Date beforeLastObserved, Date afterLastObserved,
	        Pageable page) {

		Page<Premium> results = null;
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
		if (beforeLastObserved != null)
			rule += AppConstants.BEFORE_LAST_OBSERVED;
		if (afterLastObserved != null)
			rule += AppConstants.AFTER_LAST_OBSERVED;

		log.info("findPremiumRangeByFilters(): rule: {}", rule); 
		
		switch (rule) {
		case 0:
			results = premiumRepo.findAll(page);
			break;
		case 1:
			results = premiumRepo.findPremiumRangeByRule1(codeList, page);
			break;
		case 2:
			results = premiumRepo.findPremiumRangeByRule2(iso2List, page);
			break;
		case 3:
			results = premiumRepo.findPremiumRangeByRule3(codeList, iso2List, page);
			break;
		case 4:
			results = premiumRepo.findPremiumRangeByRule4(tosList, page);
			break;
		case 5:
			results = premiumRepo.findPremiumRangeByRule5(codeList, tosList, page);
			break;
		case 6:
			results = premiumRepo.findPremiumRangeByRule6(iso2List, tosList, page);
			break;
		case 7:
			results = premiumRepo.findPremiumRangeByRule7(codeList, iso2List, tosList, page);
			break;
		case 8:
			results = premiumRepo.findPremiumRangeByRule8(tosDescList, page);
			break;
		case 9:
			results = premiumRepo.findPremiumRangeByRule9(codeList, tosDescList, page);
			break;
		case 10:
			results = premiumRepo.findPremiumRangeByRule10(iso2List, tosDescList, page);
			break;
		case 11:
			results = premiumRepo.findPremiumRangeByRule11(codeList, iso2List, tosDescList, page);
			break;
		case 12:
			results = premiumRepo.findPremiumRangeByRule12(tosList, tosDescList, page);
			break;
		case 13:
			results = premiumRepo.findPremiumRangeByRule13(codeList, tosList, tosDescList, page);
			break;
		case 14:
			results = premiumRepo.findPremiumRangeByRule14(iso2List, tosList, tosDescList, page);
			break;
		case 15:
			results = premiumRepo.findPremiumRangeByRule15(codeList, iso2List, tosList, tosDescList, page);
			break;
		case 16:
			results = premiumRepo.findPremiumRangeByRule16(providerList, page);
			break;
		case 17:
			results = premiumRepo.findPremiumRangeByRule17(codeList, providerList, page);
			break;
		case 18:
			results = premiumRepo.findPremiumRangeByRule18(iso2List, providerList, page);
			break;
		case 19:
			results = premiumRepo.findPremiumRangeByRule19(codeList, iso2List, providerList, page);
			break;
		case 20:
			results = premiumRepo.findPremiumRangeByRule20(tosList, providerList, page);
			break;
		case 21:
			results = premiumRepo.findPremiumRangeByRule21(codeList, tosList, providerList, page);
			break;
		case 22:
			results = premiumRepo.findPremiumRangeByRule22(iso2List, tosList, providerList, page);
			break;
		case 23:
			results = premiumRepo.findPremiumRangeByRule23(codeList, iso2List, tosList, providerList, page);
			break;
		case 24:
			results = premiumRepo.findPremiumRangeByRule24(tosDescList, providerList, page);
			break;
		case 25:
			results = premiumRepo.findPremiumRangeByRule25(codeList, tosDescList, providerList, page);
			break;
		case 26:
			results = premiumRepo.findPremiumRangeByRule26(iso2List, tosDescList, providerList, page);
			break;
		case 27:
			results = premiumRepo.findPremiumRangeByRule27(codeList, iso2List, tosDescList, providerList, page);
			break;
		case 28:
			results = premiumRepo.findPremiumRangeByRule28(tosList, tosDescList, providerList, page);
			break;
		case 29:
			results = premiumRepo.findPremiumRangeByRule29(codeList, tosList, tosDescList, providerList, page);
			break;
		case 30:
			results = premiumRepo.findPremiumRangeByRule30(iso2List, tosList, tosDescList, providerList, page);
			break;
		case 31:
			results = premiumRepo.findPremiumRangeByRule31(codeList, iso2List, tosList, tosDescList, providerList, page);
			break;
		case 32:
			results = premiumRepo.findPremiumRangeByRule32(afterLastObserved, page);
			break;
		case 33:
			results = premiumRepo.findPremiumRangeByRule33(codeList, afterLastObserved, page);
			break;
		case 34:
			results = premiumRepo.findPremiumRangeByRule34(iso2List, afterLastObserved, page);
			break;
		case 35:
			results = premiumRepo.findPremiumRangeByRule35(codeList, iso2List, afterLastObserved, page);
			break;
		case 36:
			results = premiumRepo.findPremiumRangeByRule36(tosList, afterLastObserved, page);
			break;
		case 37:
			results = premiumRepo.findPremiumRangeByRule37(codeList, tosList, afterLastObserved, page);
			break;
		case 38:
			results = premiumRepo.findPremiumRangeByRule38(iso2List, tosList, afterLastObserved, page);
			break;
		case 39:
			results = premiumRepo.findPremiumRangeByRule39(codeList, iso2List, tosList, afterLastObserved, page);
			break;
		case 40:
			results = premiumRepo.findPremiumRangeByRule40(tosDescList, afterLastObserved, page);
			break;
		case 41:
			results = premiumRepo.findPremiumRangeByRule41(codeList, tosDescList, afterLastObserved, page);
			break;
		case 42:
			results = premiumRepo.findPremiumRangeByRule42(iso2List, tosDescList, afterLastObserved, page);
			break;
		case 43:
			results = premiumRepo.findPremiumRangeByRule43(codeList, iso2List, tosDescList, afterLastObserved, page);
			break;
		case 44:
			results = premiumRepo.findPremiumRangeByRule44(tosList, tosDescList, afterLastObserved, page);
			break;
		case 45:
			results = premiumRepo.findPremiumRangeByRule45(codeList, tosList, tosDescList, afterLastObserved, page);
			break;
		case 46:
			results = premiumRepo.findPremiumRangeByRule46(iso2List, tosList, tosDescList, afterLastObserved, page);
			break;
		case 47:
			results = premiumRepo.findPremiumRangeByRule47(codeList, iso2List, tosList, tosDescList, afterLastObserved, page);
			break;
		case 48:
			results = premiumRepo.findPremiumRangeByRule48(providerList, afterLastObserved, page);
			break;
		case 49:
			results = premiumRepo.findPremiumRangeByRule49(codeList, providerList, afterLastObserved, page);
			break;
		case 50:
			results = premiumRepo.findPremiumRangeByRule50(iso2List, providerList, afterLastObserved, page);
			break;
		case 51:
			results = premiumRepo.findPremiumRangeByRule51(codeList, iso2List, providerList, afterLastObserved, page);
			break;
		case 52:
			results = premiumRepo.findPremiumRangeByRule52(tosList, providerList, afterLastObserved, page);
			break;
		case 53:
			results = premiumRepo.findPremiumRangeByRule53(codeList, tosList, providerList, afterLastObserved, page);
			break;
		case 54:
			results = premiumRepo.findPremiumRangeByRule54(iso2List, tosList, providerList, afterLastObserved, page);
			break;
		case 55:
			results = premiumRepo.findPremiumRangeByRule55(codeList, iso2List, tosList, providerList, afterLastObserved, page);
			break;
		case 56:
			results = premiumRepo.findPremiumRangeByRule56(tosDescList, providerList, afterLastObserved, page);
			break;
		case 57:
			results = premiumRepo.findPremiumRangeByRule57(codeList, tosDescList, providerList, afterLastObserved, page);
			break;
		case 58:
			results = premiumRepo.findPremiumRangeByRule58(iso2List, tosDescList, providerList, afterLastObserved, page);
			break;
		case 59:
			results = premiumRepo.findPremiumRangeByRule59(codeList, iso2List, tosDescList, providerList, afterLastObserved, page);
			break;
		case 60:
			results = premiumRepo.findPremiumRangeByRule60(tosList, tosDescList, providerList, afterLastObserved, page);
			break;
		case 61:
			results = premiumRepo.findPremiumRangeByRule61(codeList, tosList, tosDescList, providerList, afterLastObserved, page);
			break;
		case 62:
			results = premiumRepo.findPremiumRangeByRule62(iso2List, tosList, tosDescList, providerList, afterLastObserved, page);
			break;
		case 63:
			results = premiumRepo.findPremiumRangeByRule63(codeList, iso2List, tosList, tosDescList, providerList, afterLastObserved, page);
			break;
		case 64:
			results = premiumRepo.findPremiumRangeByRule64(beforeLastObserved, page);
			break;
		case 65:
			results = premiumRepo.findPremiumRangeByRule65(codeList, beforeLastObserved, page);
			break;
		case 66:
			results = premiumRepo.findPremiumRangeByRule66(iso2List, beforeLastObserved, page);
			break;
		case 67:
			results = premiumRepo.findPremiumRangeByRule67(codeList, iso2List, beforeLastObserved, page);
			break;
		case 68:
			results = premiumRepo.findPremiumRangeByRule68(tosList, beforeLastObserved, page);
			break;
		case 69:
			results = premiumRepo.findPremiumRangeByRule69(codeList, tosList, beforeLastObserved, page);
			break;
		case 70:
			results = premiumRepo.findPremiumRangeByRule70(iso2List, tosList, beforeLastObserved, page);
			break;
		case 71:
			results = premiumRepo.findPremiumRangeByRule71(codeList, iso2List, tosList, beforeLastObserved, page);
			break;
		case 72:
			results = premiumRepo.findPremiumRangeByRule72(tosDescList, beforeLastObserved, page);
			break;
		case 73:
			results = premiumRepo.findPremiumRangeByRule73(codeList, tosDescList, beforeLastObserved, page);
			break;
		case 74:
			results = premiumRepo.findPremiumRangeByRule74(iso2List, tosDescList, beforeLastObserved, page);
			break;
		case 75:
			results = premiumRepo.findPremiumRangeByRule75(codeList, iso2List, tosDescList, beforeLastObserved, page);
			break;
		case 76:
			results = premiumRepo.findPremiumRangeByRule76(tosList, tosDescList, beforeLastObserved, page);
			break;
		case 77:
			results = premiumRepo.findPremiumRangeByRule77(codeList, tosList, tosDescList, beforeLastObserved, page);
			break;
		case 78:
			results = premiumRepo.findPremiumRangeByRule78(iso2List, tosList, tosDescList, beforeLastObserved, page);
			break;
		case 79:
			results = premiumRepo.findPremiumRangeByRule79(codeList, iso2List, tosList, tosDescList, beforeLastObserved, page);
			break;
		case 80:
			results = premiumRepo.findPremiumRangeByRule80(providerList, beforeLastObserved, page);
			break;
		case 81:
			results = premiumRepo.findPremiumRangeByRule81(codeList, providerList, beforeLastObserved, page);
			break;
		case 82:
			results = premiumRepo.findPremiumRangeByRule82(iso2List, providerList, beforeLastObserved, page);
			break;
		case 83:
			results = premiumRepo.findPremiumRangeByRule83(codeList, iso2List, providerList, beforeLastObserved, page);
			break;
		case 84:
			results = premiumRepo.findPremiumRangeByRule84(tosList, providerList, beforeLastObserved, page);
			break;
		case 85:
			results = premiumRepo.findPremiumRangeByRule85(codeList, tosList, providerList, beforeLastObserved, page);
			break;
		case 86:
			results = premiumRepo.findPremiumRangeByRule86(iso2List, tosList, providerList, beforeLastObserved, page);
			break;
		case 87:
			results = premiumRepo.findPremiumRangeByRule87(codeList, iso2List, tosList, providerList, beforeLastObserved, page);
			break;
		case 88:
			results = premiumRepo.findPremiumRangeByRule88(tosDescList, providerList, beforeLastObserved, page);
			break;
		case 89:
			results = premiumRepo.findPremiumRangeByRule89(codeList, tosDescList, providerList, beforeLastObserved, page);
			break;
		case 90:
			results = premiumRepo.findPremiumRangeByRule90(iso2List, tosDescList, providerList, beforeLastObserved, page);
			break;
		case 91:
			results = premiumRepo.findPremiumRangeByRule91(codeList, iso2List, tosDescList, providerList, beforeLastObserved, page);
			break;
		case 92:
			results = premiumRepo.findPremiumRangeByRule92(tosList, tosDescList, providerList, beforeLastObserved, page);
			break;
		case 93:
			results = premiumRepo.findPremiumRangeByRule93(codeList, tosList, tosDescList, providerList, beforeLastObserved, page);
			break;
		case 94:
			results = premiumRepo.findPremiumRangeByRule94(iso2List, tosList, tosDescList, providerList, beforeLastObserved, page);
			break;
		case 95:
			results = premiumRepo.findPremiumRangeByRule95(codeList, iso2List, tosList, tosDescList, providerList, beforeLastObserved, page);
			break;
		case 96:
			results = premiumRepo.findPremiumRangeByRule96(afterLastObserved, beforeLastObserved, page);
			break;
		case 97:
			results = premiumRepo.findPremiumRangeByRule97(codeList, afterLastObserved, beforeLastObserved, page);
			break;
		case 98:
			results = premiumRepo.findPremiumRangeByRule98(iso2List, afterLastObserved, beforeLastObserved, page);
			break;
		case 99:
			results = premiumRepo.findPremiumRangeByRule99(codeList, iso2List, afterLastObserved, beforeLastObserved, page);
			break;
		case 100:
			results = premiumRepo.findPremiumRangeByRule100(tosList, afterLastObserved, beforeLastObserved, page);
			break;
		case 101:
			results = premiumRepo.findPremiumRangeByRule101(codeList, tosList, afterLastObserved, beforeLastObserved, page);
			break;
		case 102:
			results = premiumRepo.findPremiumRangeByRule102(iso2List, tosList, afterLastObserved, beforeLastObserved, page);
			break;
		case 103:
			results = premiumRepo.findPremiumRangeByRule103(codeList, iso2List, tosList, afterLastObserved, beforeLastObserved, page);
			break;
		case 104:
			results = premiumRepo.findPremiumRangeByRule104(tosDescList, afterLastObserved, beforeLastObserved, page);
			break;
		case 105:
			results = premiumRepo.findPremiumRangeByRule105(codeList, tosDescList, afterLastObserved, beforeLastObserved, page);
			break;
		case 106:
			results = premiumRepo.findPremiumRangeByRule106(iso2List, tosDescList, afterLastObserved, beforeLastObserved, page);
			break;
		case 107:
			results = premiumRepo.findPremiumRangeByRule107(codeList, iso2List, tosDescList, afterLastObserved, beforeLastObserved, page);
			break;
		case 108:
			results = premiumRepo.findPremiumRangeByRule108(tosList, tosDescList, afterLastObserved, beforeLastObserved, page);
			break;
		case 109:
			results = premiumRepo.findPremiumRangeByRule109(codeList, tosList, tosDescList, afterLastObserved, beforeLastObserved, page);
			break;
		case 110:
			results = premiumRepo.findPremiumRangeByRule110(iso2List, tosList, tosDescList, afterLastObserved, beforeLastObserved, page);
			break;
		case 111:
			results = premiumRepo.findPremiumRangeByRule111(codeList, iso2List, tosList, tosDescList, afterLastObserved, beforeLastObserved, page);
			break;
		case 112:
			results = premiumRepo.findPremiumRangeByRule112(providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 113:
			results = premiumRepo.findPremiumRangeByRule113(codeList, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 114:
			results = premiumRepo.findPremiumRangeByRule114(iso2List, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 115:
			results = premiumRepo.findPremiumRangeByRule115(codeList, iso2List, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 116:
			results = premiumRepo.findPremiumRangeByRule116(tosList, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 117:
			results = premiumRepo.findPremiumRangeByRule117(codeList, tosList, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 118:
			results = premiumRepo.findPremiumRangeByRule118(iso2List, tosList, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 119:
			results = premiumRepo.findPremiumRangeByRule119(codeList, iso2List, tosList, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 120:
			results = premiumRepo.findPremiumRangeByRule120(tosDescList, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 121:
			results = premiumRepo.findPremiumRangeByRule121(codeList, tosDescList, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 122:
			results = premiumRepo.findPremiumRangeByRule122(iso2List, tosDescList, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 123:
			results = premiumRepo.findPremiumRangeByRule123(codeList, iso2List, tosDescList, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 124:
			results = premiumRepo.findPremiumRangeByRule124(tosList, tosDescList, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 125:
			results = premiumRepo.findPremiumRangeByRule125(codeList, tosList, tosDescList, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 126:
			results = premiumRepo.findPremiumRangeByRule126(iso2List, tosList, tosDescList, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		case 127:
			results = premiumRepo.findPremiumRangeByRule127(codeList, iso2List, tosList, tosDescList, providerList, afterLastObserved, beforeLastObserved, page);
			break;
		default:
			log.error("Unknown rule: {}", rule);
			break;

		}

		return results;
	}

	@Override
	public Page<RangeNdc> findRangeNdcByFilters(List<String> codeList, List<String> iso2List, List<String> tosList,
	        List<String> tosDescList, List<String> providerList, Pageable page) {

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

		log.debug("findRangeNdcByFilters: query rule: {}", rule);
		switch (rule) {
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

	private void getBillingIdMap() {
		try {
			for (Object[] row : rangeNdcRepo.findAllProviders()) {
				this.billingIdProviderMap.put(row[0].toString(), row[1].toString());
			}
		} catch (Exception e) {
			log.error("Failed to get provider data:", e);
		}
	}

	@Override
	public void cleanCache() {
		this.ccNdcData.clear();
		this.countryList.clear();
		this.billingIdProviderMap.clear();
		this.tosTosDescList.clear();
	}

	@Override
	public String findProviderByBillingId(String billingId) {
		if (this.billingIdProviderMap.isEmpty()) {
			getBillingIdMap();
		}

		if (this.billingIdProviderMap.containsKey(billingId)) {
			return this.billingIdProviderMap.get(billingId);
		}

		return "";
	}

	@Override
	public List<String> findBillingIdsByProvider(String provider) {
		List<String> billingIds = new ArrayList<>();
		if (this.billingIdProviderMap.isEmpty()) {
			getBillingIdMap();
		}

		this.billingIdProviderMap.forEach((k, v) -> {
			if (v.equalsIgnoreCase(provider)) {
				billingIds.add(k);
			}

		});

		return billingIds;
	}

	@Override
	public List<ProviderBillingId> findProviders() {
		List<ProviderBillingId> results = new ArrayList<>();
		for (Object[] row : rangeNdcRepo.findAllProviders()) {
			results.add(new ProviderBillingId(row[0].toString(), row[1].toString()));
		}

		return results;
	}

	@Override
	public List<TosTosDesc> findAllTOS() {
		if (!tosTosDescList.isEmpty()) {
			return tosTosDescList;
		}
		
		for (Object[] row : rangeNdcRepo.findAllTOS()) {
			if (row.length < 2 || row[1] == null) {
				tosTosDescList.add(new TosTosDesc(row[0].toString(), ""));
			} else {
				tosTosDescList.add(new TosTosDesc(row[0].toString(), row[1].toString()));
			}
		}

		return tosTosDescList;
	}

	@Override
	public int getTotalTOSCount(String tos) {
		int count = 0;
		if (tosTosDescList.isEmpty()) {
			findAllTOS();
		}
		
		for (TosTosDesc item : tosTosDescList) {
			if (item.getTos().equalsIgnoreCase(tos)) {
				count++;
			}
		}
		
		return count;
	}


	@Override
	public Page<RangeNdc> findRangeNdcByFilters(RangeQueryFilter filter) {
		
		log.info("findRangeNdcByFilters: filter: {}", JsonHelper.toPrettyJson(filter));

		if (filter.getPageNo() == null) {
			filter.setPageNo(0);
		}

		if (filter.getLimit() == null) {
			filter.setLimit(batchSize);
		}

		if (log.isDebugEnabled())
			log.debug("findRangeNdcByFilters: receive ndc query rquest pageNo {}, limit: {}", filter.getPageNo(), filter.getLimit());

		PageRequest page = new PageRequest(filter.getPageNo(), filter.getLimit());
		List<String> codeList = filter.getCodeList();
		List<String> iso2List = filter.getIso2List();
		List<String> tosList = new ArrayList<String>();
		List<String> tosDescList = null;
		List<String> providerList = null;
		List<String> listOfTos = new ArrayList<String>();
		Map<String, List<String>> tosMap = new HashMap<String, List<String>>();
		if (filter.getTosDescList() != null && !filter.getTosDescList().isEmpty()) {
			for (TosTosDesc s : filter.getTosDescList()) {
				if (s.getTos() != null && s.getTosdesc() == null) {
					tosList.add(s.getTos());
					continue;
				}
				List<String> list =  tosMap.get(s.getTos());
				if (list == null) {
					list = new ArrayList<String>();
					tosMap.put(s.getTos(), list);
					listOfTos.add(s.getTos());
				}
				list.add(s.getTos() + "," + s.getTosdesc());
			}
		}
		for (String tos: listOfTos) {
			int tosCount = getTotalTOSCount(tos);
			if (tosMap.get(tos).size() == tosCount) {
				tosList.add(tos);
				tosMap.remove(tos);
				
			}
		}
		Set<String> tosSet = tosMap.keySet();
		Iterator it = tosSet.iterator();
		while (it.hasNext()) {
			if (tosDescList == null)
				tosDescList = new ArrayList<String>();
			
			tosDescList.addAll(tosMap.get(it.next()));
		}
		if (tosList != null && !tosList.isEmpty())
			log.info("findRangeNdcByFilters: tos filter: {}", JsonHelper.toPrettyJson(tosList));
		
		if (tosDescList != null && !tosDescList.isEmpty())
			log.info("findRangeNdcByFilters: tosDesc filter: {}", JsonHelper.toPrettyJson(tosDescList));
		
		if (filter.getProviderList() != null) {
			for (Provider p : filter.getProviderList()) {

				if (providerList == null)
					providerList = new ArrayList<String>();

				providerList.add(p.getProvider());

			}
			log.info("findRangeNdcByFilters: provider filter: {}", JsonHelper.toPrettyJson(providerList));
		}

		return  findRangeNdcByFilters(codeList, iso2List, tosList, tosDescList, providerList, page);

	}

	@Override
	public Page<Premium> findPremiumRangeByFilters(RangeQueryFilter filter) {
		log.info("findPremiumRangeByFilters: filter: {}", JsonHelper.toPrettyJson(filter));

		if (filter.getPageNo() == null) {
			filter.setPageNo(0);
		}

		if (filter.getLimit() == null) {
			filter.setLimit(batchSize);
		}

		if (log.isDebugEnabled())
			log.debug("findPremiumRangeByFilters: receive range premium query rquest pageNo {}, limit: {}", filter.getPageNo(), filter.getLimit());

		PageRequest page = new PageRequest(filter.getPageNo(), filter.getLimit());
		List<String> codeList = filter.getCodeList();
		List<String> iso2List = filter.getIso2List();
		List<String> tosList =  new ArrayList<String>();
		List<String> tosDescList = null;
		List<String> providerList = null;
		List<String> listOfTos = new ArrayList<String>();
		
		Date beforeLastObserved = null;
		Date afterLastObserved = null;
		
		Map<String, List<String>> tosMap = new HashMap<String, List<String>>();
		if (filter.getTosDescList() != null && !filter.getTosDescList().isEmpty()) {
			for (TosTosDesc s : filter.getTosDescList()) {
				if (s.getTos() != null && s.getTosdesc() == null) {
					tosList.add(s.getTos());
					continue;
				}
				List<String> list =  tosMap.get(s.getTos());
				if (list == null) {
					list = new ArrayList<String>();
					tosMap.put(s.getTos(), list);
					listOfTos.add(s.getTos());
				}
				list.add(s.getTos() + "," + s.getTosdesc());
			}
		}
		for (String tos: listOfTos) {
			int tosCount = getTotalTOSCount(tos);
			if (tosMap.get(tos).size() >= tosCount) {
				tosList.add(tos);
				tosMap.remove(tos);
				
			}
		}
		Set<String> tosSet = tosMap.keySet();
		Iterator it = tosSet.iterator();
		while (it.hasNext()) {
			if (tosDescList == null)
				tosDescList = new ArrayList<String>();
			
			tosDescList.addAll(tosMap.get(it.next()));
		}
		if (tosList != null && !tosList.isEmpty())
			log.info("findPremiumRangeByFilters: tos filter: {}", JsonHelper.toPrettyJson(tosList));
		
		if (tosDescList != null && !tosDescList.isEmpty())
			log.info("findPremiumRangeByFilters: tosDesc filter: {}", JsonHelper.toPrettyJson(tosDescList));
		

		if (filter.getProviderList() != null && !filter.getProviderList().isEmpty()) {
			for (Provider p : filter.getProviderList()) {

				if (providerList == null)
					providerList = new ArrayList<String>();

				providerList.add(p.getProvider());

			}
			log.info("findPremiumRangeByFilters: provider filter: {}", JsonHelper.toPrettyJson(providerList));
		}
		if (filter.getNumOfMonthsSinceLastObserved() != null) {
			Date observedDate = calObservedDate(filter.getNumOfMonthsSinceLastObserved());
			beforeLastObserved = null;
			afterLastObserved = observedDate;
			log.info("findPremiumRangeByFilters: last observed date: {}", observedDate);
		}
		log.info("findPremiumRangeByFilters: filter: {}", filter);
		return findPremiumRangeByFilters(codeList, iso2List, tosList, tosDescList, providerList, beforeLastObserved, afterLastObserved, page);

	}

	@Override
	public List<RangeNdc> findAllRangeNdcByFilters(RangeQueryFilter filter) {
		log.info("findRangeNdcByFilters: filter: {}", JsonHelper.toPrettyJson(filter));

		if (filter.getPageNo() == null) {
			filter.setPageNo(0);
		}

		if (filter.getLimit() == null) {
			filter.setLimit(batchSize);
		}

		if (log.isDebugEnabled())
			log.debug("findAllRangeNdcByFilters: receive ndc query rquest pageNo {}", filter.getPageNo());

		PageRequest page = new PageRequest(filter.getPageNo(), filter.getLimit());
		List<String> codeList = filter.getCodeList();
		List<String> iso2List = filter.getIso2List();
		List<String> tosList = new ArrayList<String>();
		List<String> tosDescList = null;
		List<String> providerList = null;
		List<String> listOfTos = new ArrayList<String>();
		Map<String, List<String>> tosMap = new HashMap<String, List<String>>();
		if (filter.getTosDescList() != null && !filter.getTosDescList().isEmpty()) {
			for (TosTosDesc s : filter.getTosDescList()) {
				if (s.getTos() != null && s.getTosdesc() == null) {
					tosList.add(s.getTos());
					continue;
				}
				List<String> list =  tosMap.get(s.getTos());
				if (list == null) {
					list = new ArrayList<String>();
					tosMap.put(s.getTos(), list);
					listOfTos.add(s.getTos());
				}
				list.add(s.getTos() + "," + s.getTosdesc());
			}
		}
		for (String tos: listOfTos) {
			int tosCount = getTotalTOSCount(tos);
			if (tosMap.get(tos).size() == tosCount) {
				tosList.add(tos);
				tosMap.remove(tos);
				
			}
		}
		
		Set<String> tosSet = tosMap.keySet();
		Iterator it = tosSet.iterator();
		while (it.hasNext()) {
			if (tosDescList == null)
				tosDescList = new ArrayList<String>();
			
			tosDescList.addAll(tosMap.get(it.next()));
		}
		if (tosList != null && !tosList.isEmpty())
			log.info("findAllRangeNdcByFilters: tos filter: {}", JsonHelper.toPrettyJson(tosList));
		
		if (tosDescList != null && !tosDescList.isEmpty())
			log.info("findAllRangeNdcByFilters: tosDesc filter: {}", JsonHelper.toPrettyJson(tosDescList));
		

		if (filter.getProviderList() != null) {
			for (Provider p : filter.getProviderList()) {

				if (providerList == null)
					providerList = new ArrayList<String>();

				providerList.add(p.getProvider());

			}
			log.info("findAllRangeNdcByFilters: provider filter: {}", JsonHelper.toPrettyJson(providerList));
		}

		return  findAllRangeNdcByFilters(codeList, iso2List, tosList, tosDescList, providerList);

	}

	@Override
	public List<Premium> findAllPremiumRangeByFilters(RangeQueryFilter filter) {
		log.info("findRangeNdcByFilters: filter: {}", JsonHelper.toPrettyJson(filter));

		if (filter.getPageNo() == null) {
			filter.setPageNo(0);
		}

		if (filter.getLimit() == null) {
			filter.setLimit(batchSize);
		}

		if (log.isDebugEnabled())
			log.debug("findAllPremiumRangeByFilters: receive ndc query rquest pageNo {}", filter.getPageNo());

		Date afterLastObserved = null;
		Date beforeLastObserved = null;
		
		PageRequest page = new PageRequest(filter.getPageNo(), filter.getLimit());
		List<String> codeList = filter.getCodeList();
		List<String> iso2List = filter.getIso2List();
		List<String> tosList = new ArrayList<String>();
		List<String> tosDescList = null;
		List<String> providerList = null;
		List<String> listOfTos = new ArrayList<String>();
		Map<String, List<String>> tosMap = new HashMap<String, List<String>>();
		if (filter.getTosDescList() != null && !filter.getTosDescList().isEmpty()) {
			for (TosTosDesc s : filter.getTosDescList()) {
				if (s.getTos() != null && s.getTosdesc() == null) {
					tosList.add(s.getTos());
					continue;
				}
				List<String> list =  tosMap.get(s.getTos());
				if (list == null) {
					list = new ArrayList<String>();
					tosMap.put(s.getTos(), list);
					listOfTos.add(s.getTos());
				}
				list.add(s.getTos() + "," + s.getTosdesc());
			}
		}
		for (String tos: listOfTos) {
			int tosCount = getTotalTOSCount(tos);
			if (tosMap.get(tos).size() == tosCount) {
				tosList.add(tos);
				tosMap.remove(tos);
				
			}
		}
		Set<String> tosSet = tosMap.keySet();
		Iterator it = tosSet.iterator();
		while (it.hasNext()) {
			if (tosDescList == null)
				tosDescList = new ArrayList<String>();
			
			tosDescList.addAll(tosMap.get(it.next()));
		}
		
		if (tosList != null && !tosList.isEmpty())
			log.info("findAllPremiumRangeByFilters: tos filter: {}", JsonHelper.toPrettyJson(tosList));
		
		if (tosDescList != null && !tosDescList.isEmpty())
			log.info("findAllPremiumRangeByFilters: tosDesc filter: {}", JsonHelper.toPrettyJson(tosDescList));
		
	  

		if (filter.getProviderList() != null) {
			for (Provider p : filter.getProviderList()) {

				if (providerList == null)
					providerList = new ArrayList<String>();

				providerList.add(p.getProvider());

			}
			log.info("findAllPremiumRangeByFilters: provider filter: {}", JsonHelper.toPrettyJson(providerList));
		}
		if (filter.getNumOfMonthsSinceLastObserved() != null) {
			Date observedDate = calObservedDate(filter.getNumOfMonthsSinceLastObserved());
			afterLastObserved = observedDate;
			beforeLastObserved = null;
			log.info("findAllPremiumRangeByFilters: last observed date: {}", observedDate);
		}
	
		return  findAllPremiumRangeByFilters(codeList, iso2List, tosList, tosDescList, providerList, beforeLastObserved, afterLastObserved);

	}

	
	@Override
	public List<RangeNdc> findAllRangeNdcByFilters(List<String> codeList, List<String> iso2List, List<String> tosList,
			List<String> tosDescList, List<String> providerList) {
		List<RangeNdc> results = null;
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

		log.debug("findAllRangeNdcByFilters: query rule: {}", rule);
		switch (rule) {
		case 0:
			results = rangeNdcRepo.findAll();
			break;
		case 1:
			results = rangeNdcRepo.findRangeNdcbyRule1(codeList);
			break;
		case 2:
			results = rangeNdcRepo.findRangeNdcbyRule2(iso2List);
			break;
		case 3:
			results = rangeNdcRepo.findRangeNdcbyRule3(codeList, iso2List);
			break;
		case 4:
			results = rangeNdcRepo.findRangeNdcbyRule4(tosList);
			break;
		case 5:
			results = rangeNdcRepo.findRangeNdcbyRule5(codeList, tosList);
			break;
		case 6:
			results = rangeNdcRepo.findRangeNdcbyRule6(iso2List, tosList);
			break;
		case 7:
			results = rangeNdcRepo.findRangeNdcbyRule7(codeList, iso2List, tosList);
			break;
		case 8:
			results = rangeNdcRepo.findRangeNdcbyRule8(tosDescList);
			break;
		case 9:
			results = rangeNdcRepo.findRangeNdcbyRule9(codeList, tosDescList);
			break;
		case 10:
			results = rangeNdcRepo.findRangeNdcbyRule10(iso2List, tosDescList);
			break;
		case 11:
			results = rangeNdcRepo.findRangeNdcbyRule11(codeList, iso2List, tosDescList);
			break;
		case 12:
			results = rangeNdcRepo.findRangeNdcbyRule12(tosList, tosDescList);
			break;
		case 13:
			results = rangeNdcRepo.findRangeNdcbyRule13(codeList, tosList, tosDescList);
			break;
		case 14:
			results = rangeNdcRepo.findRangeNdcbyRule14(iso2List, tosList, tosDescList);
			break;
		case 15:
			results = rangeNdcRepo.findRangeNdcbyRule15(codeList, iso2List, tosList, tosDescList);
			break;
		case 16:
			results = rangeNdcRepo.findRangeNdcbyRule16(providerList);
			break;
		case 17:
			results = rangeNdcRepo.findRangeNdcbyRule17(codeList, providerList);
			break;
		case 18:
			results = rangeNdcRepo.findRangeNdcbyRule18(iso2List, providerList);
			break;
		case 19:
			results = rangeNdcRepo.findRangeNdcbyRule19(codeList, iso2List, providerList);
			break;
		case 20:
			results = rangeNdcRepo.findRangeNdcbyRule20(tosList, providerList);
			break;
		case 21:
			results = rangeNdcRepo.findRangeNdcbyRule21(codeList, tosList, providerList);
			break;
		case 22:
			results = rangeNdcRepo.findRangeNdcbyRule22(iso2List, tosList, providerList);
			break;
		case 23:
			results = rangeNdcRepo.findRangeNdcbyRule23(codeList, iso2List, tosList, providerList);
			break;
		case 24:
			results = rangeNdcRepo.findRangeNdcbyRule24(tosDescList, providerList);
			break;
		case 25:
			results = rangeNdcRepo.findRangeNdcbyRule25(codeList, tosDescList, providerList);
			break;
		case 26:
			results = rangeNdcRepo.findRangeNdcbyRule26(iso2List, tosDescList, providerList);
			break;
		case 27:
			results = rangeNdcRepo.findRangeNdcbyRule27(codeList, iso2List, tosDescList, providerList);
			break;
		case 28:
			results = rangeNdcRepo.findRangeNdcbyRule28(tosList, tosDescList, providerList);
			break;
		case 29:
			results = rangeNdcRepo.findRangeNdcbyRule29(codeList, tosList, tosDescList, providerList);
			break;
		case 30:
			results = rangeNdcRepo.findRangeNdcbyRule30(iso2List, tosList, tosDescList, providerList);
			break;
		case 31:
			results = rangeNdcRepo.findRangeNdcbyRule31(codeList, iso2List, tosList, tosDescList, providerList);
			break;
		default:
			break;

		}

		return results;
	}

	@Override
	public List<Premium> findAllPremiumRangeByFilters(List<String> codeList, 
			                                          List<String> iso2List,
			                                          List<String> tosList, 
			                                          List<String> tosDescList, 
			                                          List<String> providerList, 
			                                          Date beforeLastObserved, 
			                                          Date afterLastObserved) {
		List<Premium> results = null;
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
		if (beforeLastObserved != null )
			rule += AppConstants.BEFORE_LAST_OBSERVED;
		if (afterLastObserved != null)
			rule += AppConstants.AFTER_LAST_OBSERVED;

		log.debug("findAllPremiumRangeByFilters: query rule: {}", rule);
		switch (rule) {
		case 0:
			results = premiumRepo.findAll();
			break;
		case 1:
			results = premiumRepo.findPremiumRangeByRule1(codeList);
			break;
		case 2:
			results = premiumRepo.findPremiumRangeByRule2(iso2List);
			break;
		case 3:
			results = premiumRepo.findPremiumRangeByRule3(codeList, iso2List);
			break;
		case 4:
			results = premiumRepo.findPremiumRangeByRule4(tosList);
			break;
		case 5:
			results = premiumRepo.findPremiumRangeByRule5(codeList, tosList);
			break;
		case 6:
			results = premiumRepo.findPremiumRangeByRule6(iso2List, tosList);
			break;
		case 7:
			results = premiumRepo.findPremiumRangeByRule7(codeList, iso2List, tosList);
			break;
		case 8:
			results = premiumRepo.findPremiumRangeByRule8(tosDescList);
			break;
		case 9:
			results = premiumRepo.findPremiumRangeByRule9(codeList, tosDescList);
			break;
		case 10:
			results = premiumRepo.findPremiumRangeByRule10(iso2List, tosDescList);
			break;
		case 11:
			results = premiumRepo.findPremiumRangeByRule11(codeList, iso2List, tosDescList);
			break;
		case 12:
			results = premiumRepo.findPremiumRangeByRule12(tosList, tosDescList);
			break;
		case 13:
			results = premiumRepo.findPremiumRangeByRule13(codeList, tosList, tosDescList);
			break;
		case 14:
			results = premiumRepo.findPremiumRangeByRule14(iso2List, tosList, tosDescList);
			break;
		case 15:
			results = premiumRepo.findPremiumRangeByRule15(codeList, iso2List, tosList, tosDescList);
			break;
		case 16:
			results = premiumRepo.findPremiumRangeByRule16(providerList);
			break;
		case 17:
			results = premiumRepo.findPremiumRangeByRule17(codeList, providerList);
			break;
		case 18:
			results = premiumRepo.findPremiumRangeByRule18(iso2List, providerList);
			break;
		case 19:
			results = premiumRepo.findPremiumRangeByRule19(codeList, iso2List, providerList);
			break;
		case 20:
			results = premiumRepo.findPremiumRangeByRule20(tosList, providerList);
			break;
		case 21:
			results = premiumRepo.findPremiumRangeByRule21(codeList, tosList, providerList);
			break;
		case 22:
			results = premiumRepo.findPremiumRangeByRule22(iso2List, tosList, providerList);
			break;
		case 23:
			results = premiumRepo.findPremiumRangeByRule23(codeList, iso2List, tosList, providerList);
			break;
		case 24:
			results = premiumRepo.findPremiumRangeByRule24(tosDescList, providerList);
			break;
		case 25:
			results = premiumRepo.findPremiumRangeByRule25(codeList, tosDescList, providerList);
			break;
		case 26:
			results = premiumRepo.findPremiumRangeByRule26(iso2List, tosDescList, providerList);
			break;
		case 27:
			results = premiumRepo.findPremiumRangeByRule27(codeList, iso2List, tosDescList, providerList);
			break;
		case 28:
			results = premiumRepo.findPremiumRangeByRule28(tosList, tosDescList, providerList);
			break;
		case 29:
			results = premiumRepo.findPremiumRangeByRule29(codeList, tosList, tosDescList, providerList);
			break;
		case 30:
			results = premiumRepo.findPremiumRangeByRule30(iso2List, tosList, tosDescList, providerList);
			break;
		case 31:
			results = premiumRepo.findPremiumRangeByRule31(codeList, iso2List, tosList, tosDescList, providerList);
			break;
		case 32:
			results = premiumRepo.findPremiumRangeByRule32(afterLastObserved);
			break;
		case 33:
			results = premiumRepo.findPremiumRangeByRule33(codeList, afterLastObserved);
			break;
		case 34:
			results = premiumRepo.findPremiumRangeByRule34(iso2List, afterLastObserved);
			break;
		case 35:
			results = premiumRepo.findPremiumRangeByRule35(codeList, iso2List, afterLastObserved);
			break;
		case 36:
			results = premiumRepo.findPremiumRangeByRule36(tosList, afterLastObserved);
			break;
		case 37:
			results = premiumRepo.findPremiumRangeByRule37(codeList, tosList, afterLastObserved);
			break;
		case 38:
			results = premiumRepo.findPremiumRangeByRule38(iso2List, tosList, afterLastObserved);
			break;
		case 39:
			results = premiumRepo.findPremiumRangeByRule39(codeList, iso2List, tosList, afterLastObserved);
			break;
		case 40:
			results = premiumRepo.findPremiumRangeByRule40(tosDescList, afterLastObserved);
			break;
		case 41:
			results = premiumRepo.findPremiumRangeByRule41(codeList, tosDescList, afterLastObserved);
			break;
		case 42:
			results = premiumRepo.findPremiumRangeByRule42(iso2List, tosDescList, afterLastObserved);
			break;
		case 43:
			results = premiumRepo.findPremiumRangeByRule43(codeList, iso2List, tosDescList, afterLastObserved);
			break;
		case 44:
			results = premiumRepo.findPremiumRangeByRule44(tosList, tosDescList, afterLastObserved);
			break;
		case 45:
			results = premiumRepo.findPremiumRangeByRule45(codeList, tosList, tosDescList, afterLastObserved);
			break;
		case 46:
			results = premiumRepo.findPremiumRangeByRule46(iso2List, tosList, tosDescList, afterLastObserved);
			break;
		case 47:
			results = premiumRepo.findPremiumRangeByRule47(codeList, iso2List, tosList, tosDescList, afterLastObserved);
			break;
		case 48:
			results = premiumRepo.findPremiumRangeByRule48(providerList, afterLastObserved);
			break;
		case 49:
			results = premiumRepo.findPremiumRangeByRule49(codeList, providerList, afterLastObserved);
			break;
		case 50:
			results = premiumRepo.findPremiumRangeByRule50(iso2List, providerList, afterLastObserved);
			break;
		case 51:
			results = premiumRepo.findPremiumRangeByRule51(codeList, iso2List, providerList, afterLastObserved);
			break;
		case 52:
			results = premiumRepo.findPremiumRangeByRule52(tosList, providerList, afterLastObserved);
			break;
		case 53:
			results = premiumRepo.findPremiumRangeByRule53(codeList, tosList, providerList, afterLastObserved);
			break;
		case 54:
			results = premiumRepo.findPremiumRangeByRule54(iso2List, tosList, providerList, afterLastObserved);
			break;
		case 55:
			results = premiumRepo.findPremiumRangeByRule55(codeList, iso2List, tosList, providerList, afterLastObserved);
			break;
		case 56:
			results = premiumRepo.findPremiumRangeByRule56(tosDescList, providerList, afterLastObserved);
			break;
		case 57:
			results = premiumRepo.findPremiumRangeByRule57(codeList, tosDescList, providerList, afterLastObserved);
			break;
		case 58:
			results = premiumRepo.findPremiumRangeByRule58(iso2List, tosDescList, providerList, afterLastObserved);
			break;
		case 59:
			results = premiumRepo.findPremiumRangeByRule59(codeList, iso2List, tosDescList, providerList, afterLastObserved);
			break;
		case 60:
			results = premiumRepo.findPremiumRangeByRule60(tosList, tosDescList, providerList, afterLastObserved);
			break;
		case 61:
			results = premiumRepo.findPremiumRangeByRule61(codeList, tosList, tosDescList, providerList, afterLastObserved);
			break;
		case 62:
			results = premiumRepo.findPremiumRangeByRule62(iso2List, tosList, tosDescList, providerList, afterLastObserved);
			break;
		case 63:
			results = premiumRepo.findPremiumRangeByRule63(codeList, iso2List, tosList, tosDescList, providerList, afterLastObserved);
			break;
		case 64:
			results = premiumRepo.findPremiumRangeByRule64(beforeLastObserved);
			break;
		case 65:
			results = premiumRepo.findPremiumRangeByRule65(codeList, beforeLastObserved);
			break;
		case 66:
			results = premiumRepo.findPremiumRangeByRule66(iso2List, beforeLastObserved);
			break;
		case 67:
			results = premiumRepo.findPremiumRangeByRule67(codeList, iso2List, beforeLastObserved);
			break;
		case 68:
			results = premiumRepo.findPremiumRangeByRule68(tosList, beforeLastObserved);
			break;
		case 69:
			results = premiumRepo.findPremiumRangeByRule69(codeList, tosList, beforeLastObserved);
			break;
		case 70:
			results = premiumRepo.findPremiumRangeByRule70(iso2List, tosList, beforeLastObserved);
			break;
		case 71:
			results = premiumRepo.findPremiumRangeByRule71(codeList, iso2List, tosList, beforeLastObserved);
			break;
		case 72:
			results = premiumRepo.findPremiumRangeByRule72(tosDescList, beforeLastObserved);
			break;
		case 73:
			results = premiumRepo.findPremiumRangeByRule73(codeList, tosDescList, beforeLastObserved);
			break;
		case 74:
			results = premiumRepo.findPremiumRangeByRule74(iso2List, tosDescList, beforeLastObserved);
			break;
		case 75:
			results = premiumRepo.findPremiumRangeByRule75(codeList, iso2List, tosDescList, beforeLastObserved);
			break;
		case 76:
			results = premiumRepo.findPremiumRangeByRule76(tosList, tosDescList, beforeLastObserved);
			break;
		case 77:
			results = premiumRepo.findPremiumRangeByRule77(codeList, tosList, tosDescList, beforeLastObserved);
			break;
		case 78:
			results = premiumRepo.findPremiumRangeByRule78(iso2List, tosList, tosDescList, beforeLastObserved);
			break;
		case 79:
			results = premiumRepo.findPremiumRangeByRule79(codeList, iso2List, tosList, tosDescList, beforeLastObserved);
			break;
		case 80:
			results = premiumRepo.findPremiumRangeByRule80(providerList, beforeLastObserved);
			break;
		case 81:
			results = premiumRepo.findPremiumRangeByRule81(codeList, providerList, beforeLastObserved);
			break;
		case 82:
			results = premiumRepo.findPremiumRangeByRule82(iso2List, providerList, beforeLastObserved);
			break;
		case 83:
			results = premiumRepo.findPremiumRangeByRule83(codeList, iso2List, providerList, beforeLastObserved);
			break;
		case 84:
			results = premiumRepo.findPremiumRangeByRule84(tosList, providerList, beforeLastObserved);
			break;
		case 85:
			results = premiumRepo.findPremiumRangeByRule85(codeList, tosList, providerList, beforeLastObserved);
			break;
		case 86:
			results = premiumRepo.findPremiumRangeByRule86(iso2List, tosList, providerList, beforeLastObserved);
			break;
		case 87:
			results = premiumRepo.findPremiumRangeByRule87(codeList, iso2List, tosList, providerList, beforeLastObserved);
			break;
		case 88:
			results = premiumRepo.findPremiumRangeByRule88(tosDescList, providerList, beforeLastObserved);
			break;
		case 89:
			results = premiumRepo.findPremiumRangeByRule89(codeList, tosDescList, providerList, beforeLastObserved);
			break;
		case 90:
			results = premiumRepo.findPremiumRangeByRule90(iso2List, tosDescList, providerList, beforeLastObserved);
			break;
		case 91:
			results = premiumRepo.findPremiumRangeByRule91(codeList, iso2List, tosDescList, providerList, beforeLastObserved);
			break;
		case 92:
			results = premiumRepo.findPremiumRangeByRule92(tosList, tosDescList, providerList, beforeLastObserved);
			break;
		case 93:
			results = premiumRepo.findPremiumRangeByRule93(codeList, tosList, tosDescList, providerList, beforeLastObserved);
			break;
		case 94:
			results = premiumRepo.findPremiumRangeByRule94(iso2List, tosList, tosDescList, providerList, beforeLastObserved);
			break;
		case 95:
			results = premiumRepo.findPremiumRangeByRule95(codeList, iso2List, tosList, tosDescList, providerList, beforeLastObserved);
			break;
		case 96:
			results = premiumRepo.findPremiumRangeByRule96(afterLastObserved, beforeLastObserved);
			break;
		case 97:
			results = premiumRepo.findPremiumRangeByRule97(codeList, afterLastObserved, beforeLastObserved);
			break;
		case 98:
			results = premiumRepo.findPremiumRangeByRule98(iso2List, afterLastObserved, beforeLastObserved);
			break;
		case 99:
			results = premiumRepo.findPremiumRangeByRule99(codeList, iso2List, afterLastObserved, beforeLastObserved);
			break;
		case 100:
			results = premiumRepo.findPremiumRangeByRule100(tosList, afterLastObserved, beforeLastObserved);
			break;
		case 101:
			results = premiumRepo.findPremiumRangeByRule101(codeList, tosList, afterLastObserved, beforeLastObserved);
			break;
		case 102:
			results = premiumRepo.findPremiumRangeByRule102(iso2List, tosList, afterLastObserved, beforeLastObserved);
			break;
		case 103:
			results = premiumRepo.findPremiumRangeByRule103(codeList, iso2List, tosList, afterLastObserved, beforeLastObserved);
			break;
		case 104:
			results = premiumRepo.findPremiumRangeByRule104(tosDescList, afterLastObserved, beforeLastObserved);
			break;
		case 105:
			results = premiumRepo.findPremiumRangeByRule105(codeList, tosDescList, afterLastObserved, beforeLastObserved);
			break;
		case 106:
			results = premiumRepo.findPremiumRangeByRule106(iso2List, tosDescList, afterLastObserved, beforeLastObserved);
			break;
		case 107:
			results = premiumRepo.findPremiumRangeByRule107(codeList, iso2List, tosDescList, afterLastObserved, beforeLastObserved);
			break;
		case 108:
			results = premiumRepo.findPremiumRangeByRule108(tosList, tosDescList, afterLastObserved, beforeLastObserved);
			break;
		case 109:
			results = premiumRepo.findPremiumRangeByRule109(codeList, tosList, tosDescList, afterLastObserved, beforeLastObserved);
			break;
		case 110:
			results = premiumRepo.findPremiumRangeByRule110(iso2List, tosList, tosDescList, afterLastObserved, beforeLastObserved);
			break;
		case 111:
			results = premiumRepo.findPremiumRangeByRule111(codeList, iso2List, tosList, tosDescList, afterLastObserved, beforeLastObserved);
			break;
		case 112:
			results = premiumRepo.findPremiumRangeByRule112(providerList, afterLastObserved, beforeLastObserved);
			break;
		case 113:
			results = premiumRepo.findPremiumRangeByRule113(codeList, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 114:
			results = premiumRepo.findPremiumRangeByRule114(iso2List, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 115:
			results = premiumRepo.findPremiumRangeByRule115(codeList, iso2List, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 116:
			results = premiumRepo.findPremiumRangeByRule116(tosList, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 117:
			results = premiumRepo.findPremiumRangeByRule117(codeList, tosList, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 118:
			results = premiumRepo.findPremiumRangeByRule118(iso2List, tosList, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 119:
			results = premiumRepo.findPremiumRangeByRule119(codeList, iso2List, tosList, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 120:
			results = premiumRepo.findPremiumRangeByRule120(tosDescList, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 121:
			results = premiumRepo.findPremiumRangeByRule121(codeList, tosDescList, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 122:
			results = premiumRepo.findPremiumRangeByRule122(iso2List, tosDescList, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 123:
			results = premiumRepo.findPremiumRangeByRule123(codeList, iso2List, tosDescList, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 124:
			results = premiumRepo.findPremiumRangeByRule124(tosList, tosDescList, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 125:
			results = premiumRepo.findPremiumRangeByRule125(codeList, tosList, tosDescList, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 126:
			results = premiumRepo.findPremiumRangeByRule126(iso2List, tosList, tosDescList, providerList, afterLastObserved, beforeLastObserved);
			break;
		case 127:
			results = premiumRepo.findPremiumRangeByRule127(codeList, iso2List, tosList, tosDescList, providerList, afterLastObserved, beforeLastObserved);
			break;
		default:
			log.error("Unknown rule: {}", rule);
			break;

		}

		return results;
		 
	}

	public Date calObservedDate(int numOfMonthFromCurrentMonth) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MONTH, -1 * numOfMonthFromCurrentMonth);
		
		return cal.getTime();

	}

	@Override
	public String getLastDataSetDate() {
		EventNotification event = eventRepo.findTop1ByEventTypeOrderByCreateTimestampDesc(EventTypeDefinition.MobileIdUpdate.value());
		
		if (event == null) {
			log.error("Did not find data set updata event");
			return null;
		}

		return DateTimeHelper.formatDate(event.getCreateTimestamp(), "yyyyMMdd");
	}

}
