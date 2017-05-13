package com.iconectiv.irsf.portal.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.iconectiv.irsf.portal.core.EventTypeDefinition;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.repositories.common.EventNotificationRepository;
import com.iconectiv.irsf.util.JsonHelper;

@Controller
public class WebSocketController {
	private static Logger log = LoggerFactory.getLogger(WebSocketController.class);

	
	@Autowired
	EventNotificationRepository eventRepo;
	
	@MessageMapping("/dataSetUpdate")
    @SendTo("/topic/dataSetUpdateEvent")
    public EventNotification sendDataSetUpdateEvent() throws Exception {
		//Map<String, Object> attrs = headerAccessor.getSessionAttributes();
		if (log.isDebugEnabled()) log.debug("receving web socket request: ");
		EventNotification event = eventRepo.findTop1ByEventTypeOrderByCreateTimestampDesc(EventTypeDefinition.MobileIdUpdate.value());

        return event;
    }
}
