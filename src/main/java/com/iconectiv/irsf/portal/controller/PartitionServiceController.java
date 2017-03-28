package com.iconectiv.irsf.portal.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.service.PartitionService;
import com.iconectiv.irsf.util.JsonHelper;


@Controller
class PartitionServiceController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(PartitionServiceController.class);

	@Autowired
	private PartitionService service;

	@RequestMapping(value = "/partition/{partitionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getListDetails(@RequestHeader Map<String, String> header, @PathVariable Integer partitionId) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			PartitionDefinition partition = service.getPartitionDetails(partitionId);
            rv = makeSuccessResult(MessageDefinition.Query_Success, partition);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}
}
