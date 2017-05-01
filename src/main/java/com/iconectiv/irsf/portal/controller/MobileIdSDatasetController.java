package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.*;
import com.iconectiv.irsf.portal.repositories.common.CountryRepository;
import com.iconectiv.irsf.portal.repositories.common.IprnRepository;
import com.iconectiv.irsf.portal.repositories.common.PremiumRepository;
import com.iconectiv.irsf.portal.repositories.common.RangeNdcRepository;
import com.iconectiv.irsf.portal.service.MobileIdDataService;
import com.iconectiv.irsf.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
class MobileIdDatasetController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(MobileIdDatasetController.class);

	@Value("${jdbc.query_batch_size:100000}")
	private int batchSize;
	@Autowired
	private PremiumRepository premiumRepo;
	@Autowired
	private CountryRepository countryRepo;
	@Autowired
	private RangeNdcRepository rangeNdcRepo;
	@Autowired
	private MobileIdDataService mobileIdDataService;

	@RequestMapping(value = "/premium", method = RequestMethod.GET)
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

			Page<Premium> results = premiumRepo.findAll(page);
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

		return rv;
	}

	@RequestMapping(value = "/providers", method = RequestMethod.GET)
	public ResponseEntity<String> getProviders(@RequestHeader Map<String, String> header) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			List<ProviderBillingId> results = mobileIdDataService.findProviders();
			rv = makeSuccessResult("", results);

		} catch (Exception e) {
			log.error("Error to retieve provider data", e);
			rv = makeErrorResult(e);
		}

		return rv;
	}

	@RequestMapping(value = "/rangeNDC", method = RequestMethod.GET)
	public ResponseEntity<String> getRangeNDC(@RequestHeader Map<String, String> header,
	        @RequestParam(value = "pageNo", required = false) Integer pageNo,
	        @RequestParam(value = "limit", required = false) Integer limit) {
		ResponseEntity<String> rv;
		try {
			if (log.isDebugEnabled())
				log.debug("receive rangeNDC query rquest pageNo {}", pageNo);
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

	@RequestMapping(value = "/findRangeNdc", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> getRangeNDC(@RequestHeader Map<String, String> header,
	        @RequestBody String value, Locale locale) {
		ResponseEntity<String> rv;
		try {

			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());
			
			log.info("/findRangeNdc: filter: {}", value);
			RangeQueryFilter filter = JsonHelper.fromJson(value, RangeQueryFilter.class);
			
			Page<RangeNdc> results = mobileIdDataService.findRangeNdcByFilters(filter);

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

	@RequestMapping(value = "/findPremium", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> getPremiumrange(@RequestHeader Map<String, String> header,
	        @RequestBody String value, Locale locale) {
		ResponseEntity<String> rv;
		try {

			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());
			
			log.info("/findPremium: filter: {}", value);
			RangeQueryFilter filter = JsonHelper.fromJson(value, RangeQueryFilter.class);

			Page<Premium> results = mobileIdDataService.findPremiumRangeByFilters(filter);

			rv = makeSuccessResult(results);

		} catch (Exception e) {
			log.error("Error to retrieve premium range data", e);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug("Completed query premium range request");
		}
		return rv;
	}
}
