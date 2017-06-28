package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.core.EventTypeDefinition;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.repositories.common.EventNotificationRepository;
import com.iconectiv.irsf.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

//@Controller
public class WebSocketController {
	private static Logger log = LoggerFactory.getLogger(WebSocketController.class);

	
	@Autowired
	EventNotificationRepository eventRepo;
	
	@MessageMapping("/lastDataSetUpdate")
    @SendTo("/topic/dataSetUpdateEvent")
    public EventNotification sendDataSetUpdateEvent() throws Exception {
		EventNotification event = eventRepo.findTop1ByEventTypeOrderByCreateTimestampDesc(EventTypeDefinition.MobileIdUpdate.value());
		
		if (log.isDebugEnabled()) log.debug("return websocket message: " + JsonHelper.toJson(event));
        return event;
    }
}
