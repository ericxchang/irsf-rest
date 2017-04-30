package com.iconectiv.irsf.portal.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.service.EventNotificationService;
import com.iconectiv.irsf.portal.service.PartitionService;
import com.iconectiv.irsf.util.DateTimeHelper;
import com.iconectiv.irsf.util.JsonHelper;


@Controller
class EventServiceController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(EventServiceController.class);

	@Autowired
	private EventNotificationService eventService;

	@RequestMapping(value = "/event", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getEvents(@RequestHeader Map<String, String> header,
			@RequestParam(value = "lastQueryTime", required = false) long lastQueryTime) {
		ResponseEntity<String> rv = null;
		try {
			UserDefinition loginUser = getLoginUser(header);
			
			if (loginUser.getRole().equals(PermissionRole.CustAdmin.value()) || loginUser.getRole().equals(PermissionRole.User.value()) ) {
				List<EventNotification>	events = eventService.getEvents(loginUser, new Date(lastQueryTime));
			    rv = makeSuccessResult(MessageDefinition.Query_Success, events);
			}
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
