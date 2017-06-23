package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.*;
import com.iconectiv.irsf.portal.repositories.common.CountryRepository;
import com.iconectiv.irsf.portal.repositories.common.PremiumRepository;
import com.iconectiv.irsf.portal.repositories.common.RangeNdcRepository;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.portal.service.FileHandlerService;
import com.iconectiv.irsf.portal.service.MobileIdDataService;
import com.iconectiv.irsf.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
class MobileIdDatasetController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(MobileIdDatasetController.class);

    @Value("${jdbc.gui_query_batch_size:10000}")
	private int batchSize;
	@Autowired
	private PremiumRepository premiumRepo;
	@Autowired
	private CountryRepository countryRepo;
	@Autowired
	private RangeNdcRepository rangeNdcRepo;
	@Autowired
	private MobileIdDataService mobileIdDataService;
	@Autowired
	private FileHandlerService fileService;
	@Autowired
	private AuditTrailService auditService;
	
	@Value("${range_data_location:/apps/irsf/data/unload/range_ndc.csv}")
	private String rangeFileLocation;
	
	@Value("${premium_data_location:/apps/irsf/data/unload/premium.csv}")
	private String premiumFileLocation;
	

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

	@RequestMapping(value = "/tos", method = RequestMethod.GET)
	public ResponseEntity<String> getTOS(@RequestHeader Map<String, String> header) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			List<TosTosDesc> results = mobileIdDataService.findAllTOS();
			rv = makeSuccessResult("", results);

		} catch (Exception e) {
			log.error("Error to retieve tos data", e);
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


	@RequestMapping(value = "/download/{type}", method = RequestMethod.GET)
	public HttpEntity<byte[]> downloadMobileIdDataSet(@RequestHeader Map<String, String> header, @PathVariable String type) throws Exception{
		if (log.isDebugEnabled()) log.debug("Received download mobileID data set request");
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			byte[] documentBody;
			String dataFileName;
			String outputFileName;
			
			if (type.equalsIgnoreCase("iprn")) {
				dataFileName = premiumFileLocation;
				outputFileName = "IPRN-" + mobileIdDataService.getLastDataSetDate() + ".csv";
			} else {
				dataFileName = rangeFileLocation;
				outputFileName = "RangeNDC-" + mobileIdDataService.getLastDataSetDate() + ".csv";
			}
			
			documentBody = fileService.getContent(dataFileName);
			
			HttpHeaders respHeader = new HttpHeaders();
			
			respHeader.set("Content-Disposition", "attachment; filename=" + outputFileName);
			respHeader.setContentLength(documentBody.length);

			return new HttpEntity<byte[]>(documentBody, respHeader);
			
		} catch(Exception e) {
			log.error("Error to download mobileId data: ", e);
			//TODO throw SNMP trap
		}
		
		return null;
	}
}
