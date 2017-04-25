package com.iconectiv.irsf.portal.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iconectiv.irsf.portal.core.AppConstants;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.Country;
import com.iconectiv.irsf.portal.model.common.Iprn;
import com.iconectiv.irsf.portal.model.common.RangeNdc;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.repositories.common.CountryRepository;
import com.iconectiv.irsf.portal.repositories.common.IprnRepository;
import com.iconectiv.irsf.portal.repositories.common.RangeNdcRepository;
import com.iconectiv.irsf.portal.service.MobileIdDataService;

@Controller
class MobileIdDatasetController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(MobileIdDatasetController.class);

	@Value("${jdbc.query_batch_size:100000}")
	private int batchSize;
	@Autowired
	private IprnRepository iprnRepo;
	@Autowired
	private CountryRepository countryRepo;
	@Autowired
	private RangeNdcRepository rangeNdcRepo;
	@Autowired
	private MobileIdDataService mobileIdDataService;

	@RequestMapping(value = "/iprn", method = RequestMethod.GET)
	public ResponseEntity<String> getIPRN(@RequestHeader Map<String, String> header,
	        @RequestParam(value = "pageNo", required = false) Integer pageNo,
	        @RequestParam(value = "limit", required = false) Integer limit) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			if (pageNo == null) {
				pageNo = 0;
			}

			if (limit == null) {
				limit = batchSize;
			}

			PageRequest page = new PageRequest(pageNo, limit);

			Page<Iprn> results = iprnRepo.findAll(page);
			rv = makeSuccessResult(results);
		} catch (NullPointerException e1) {
			rv = makeErrorResult(e1);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		return rv;
	}

	@RequestMapping(value = "/country", method = RequestMethod.GET)
	public ResponseEntity<String> getCountry(@RequestHeader Map<String, String> header,
	        @RequestParam(value = "pageNo", required = false) Integer pageNo,
	        @RequestParam(value = "limit", required = false) Integer limit) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			if (limit == null) {
				limit = batchSize;
			}

			if (pageNo != null) {
				PageRequest page = new PageRequest(pageNo, limit);

				Page<Country> results = countryRepo.findAll(page);
				rv = makeSuccessResult(results);
			} else {
				Iterable<Country> results = countryRepo.findAll();
				rv = makeSuccessResult("", results);
			}

		} catch (Exception e) {
			log.error("Error to retieve country data", e);
			rv = makeErrorResult(e);
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Completed query country request");
		}
		return rv;
	}

	@RequestMapping(value = "/rangeNDC", method = RequestMethod.GET)
	public ResponseEntity<String> getRangeNDC(@RequestHeader Map<String, String> header,
	        @RequestParam(value = "pageNo", required = false) Integer pageNo,
	        @RequestParam(value = "limit", required = false) Integer limit) {
		ResponseEntity<String> rv;
		try {
			if (log.isDebugEnabled()) log.debug("receive rangeNDC query rquest pageNo {}", pageNo);
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			if (pageNo == null) {
				pageNo = 0;
			}

			if (limit == null) {
				limit = batchSize;
			}

			PageRequest page = new PageRequest(pageNo, limit);

			Page<RangeNdc> results = rangeNdcRepo.findAll(page);
			rv = makeSuccessResult(results);
			

		} catch (Exception e) {
			log.error("Error to retrieve country data", e);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug("Completed query rangeNDC  request");
		}
		return rv;
	}
	
	@RequestMapping(value = "/ndc", method = RequestMethod.GET)
	public ResponseEntity<String> getGlobalRangeNDC(@RequestHeader Map<String, String> header,
			@RequestParam(value = "codeList", required = false) String listOfCodes,
			@RequestParam(value = "iso2List", required = false) String listOfIso2,
			@RequestParam(value = "tosList", required = false)  String listOfTos,
			@RequestParam(value = "tosDescList", required = false)  String listOfTosDescs,
			@RequestParam(value = "providerList", required = false)  String listOfProviders,
	        @RequestParam(value = "pageNo", required = false) Integer pageNo,
	        @RequestParam(value = "limit", required = false) Integer limit) {
		ResponseEntity<String> rv;
		try {
			if (log.isDebugEnabled()) log.debug("receive ndc query rquest pageNo {}", pageNo);
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			if (pageNo == null) {
				pageNo = 0;
			}

			if (limit == null) {
				limit = batchSize;
			}

			PageRequest page = new PageRequest(pageNo, limit);
			List<String> codeList = new ArrayList<String>();
			List<String> iso2List = new ArrayList<String>();
			List<String> tosList = new ArrayList<String>();
			List<String> tosDescList = new ArrayList<String>();
			List<String> providerList = new ArrayList<String>();
			
			int rule = 0;
			if (listOfCodes != null && !listOfCodes.isEmpty()) {
				codeList = Arrays.asList(listOfCodes.split("|"));
				rule += AppConstants.CODE;
			}
			if (listOfIso2 != null && !listOfIso2.isEmpty()) {
				iso2List = Arrays.asList(listOfIso2.split("|"));
				rule += AppConstants.ISO2;
			}
			if (listOfTos != null && !listOfTos.isEmpty()) {
				tosList = Arrays.asList(listOfTos.split("|"));
				rule += AppConstants.TOS;
			}
			if (listOfTosDescs != null && !listOfTosDescs.isEmpty()) {
				tosDescList = Arrays.asList(listOfTosDescs.split("|"));
				rule += AppConstants.TOSDESC;
			}
			if (listOfProviders != null && !listOfProviders.isEmpty()) {
				providerList = Arrays.asList(listOfProviders.split("|"));
				rule += AppConstants.PROVIDER;
			}
			
			Page<RangeNdc> results = mobileIdDataService.findRangeNdcbyFilters(codeList, iso2List, tosList, tosDescList, providerList, page);
			rv = makeSuccessResult(results);
			

		} catch (Exception e) {
			log.error("Error to retrieve rangeNDC data", e);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug("Completed query rangeNDC request");
		}
		return rv;
	}
}
