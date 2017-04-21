package com.iconectiv.irsf.portal.controller;

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

import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.Country;
import com.iconectiv.irsf.portal.model.common.Iprn;
import com.iconectiv.irsf.portal.model.common.RangeNdc;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.repositories.common.CountryRepository;
import com.iconectiv.irsf.portal.repositories.common.IprnRepository;
import com.iconectiv.irsf.portal.repositories.common.RangeNdcRepository;

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
}
