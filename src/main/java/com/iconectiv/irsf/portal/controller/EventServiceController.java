package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.service.EventNotificationService;
import com.iconectiv.irsf.util.DateTimeHelper;
import com.iconectiv.irsf.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
class EventServiceController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(EventServiceController.class);

	@Autowired
	private EventNotificationService eventService;

	@RequestMapping(value = "/userEvents", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getEvents(@RequestHeader Map<String, String> header,
			@RequestParam(value = "lastQueryTime", required = false) String lastQueryTime) {
		ResponseEntity<String> rv = null;
        Date queryTime = null;
		try {
			UserDefinition loginUser = getLoginUser(header);
			List<EventNotification>	events;
            try {
                queryTime = DateTimeHelper.formatDate(lastQueryTime, "yyyy-MM-dd HH:mm:SS z");
            } catch (Exception e) {
                log.warn(e.getMessage());
            }

			if (loginUser.getRole().equals(PermissionRole.CustAdmin.value()) || loginUser.getRole().equals(PermissionRole.User.value()) ) {

                if (queryTime == null) {
                    events = eventService.getEvents(loginUser, null);
                } else {
                    events = eventService.getEvents(loginUser, DateTimeHelper.toUTC(queryTime));
                }

                lastQueryTime = DateTimeHelper.formatDate( DateTimeHelper.nowInUTC(), "yyyy-MM-dd HH:mm:SS z");
			    rv = makeSuccessResult(lastQueryTime, events);
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
