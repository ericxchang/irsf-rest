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

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.service.PartitionService;
import com.iconectiv.irsf.util.JsonHelper;


@Controller
class PartitionServiceController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(PartitionServiceController.class);

	@Autowired
	private PartitionService service;

	@RequestMapping(value = "/partition/{schema}/{partitionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getListDetails(@PathVariable String schema, @PathVariable Integer partitionId) {
		ResponseEntity<String> rv;
		try {
			CustomerContextHolder.setSchema(schema);
			PartitionDefinition partition = service.getPartitionDetails(partitionId);
            rv = makeSuccessResult(partition);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}
}
