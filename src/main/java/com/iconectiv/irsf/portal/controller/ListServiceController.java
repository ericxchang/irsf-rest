package com.iconectiv.irsf.portal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iconectiv.irsf.portal.repositories.common.ListUploadRequestRepository;
import com.iconectiv.irsf.portal.util.JsonHelper;

@Controller
class ListServiceController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(ListServiceController.class);

	@Autowired
	private ListUploadRequestRepository uploadRequestRepo;

	@RequestMapping(value = "/list/uploadrequests/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getUploadRequest(@PathVariable String type) {
		ResponseEntity<String> rv;
		try {
			rv = makeSuccessResult(uploadRequestRepo.findAllByType(type));
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toJson(rv));
		}
		return rv;
	}

}
