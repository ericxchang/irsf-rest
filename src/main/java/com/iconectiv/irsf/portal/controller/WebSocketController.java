package com.iconectiv.irsf.portal.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.service.EventNotificationService;

@Controller
public class WebSocketController {
	private static Logger log = LoggerFactory.getLogger(WebSocketController.class);

	@Autowired
	EventNotificationService eventService;
	
    @SendTo("/topic/event")
    public List<EventNotification> sendEvent() throws Exception {
    	List<EventNotification> events = eventService.getEvents();
    	
        Thread.sleep(1000); // simulated delay
        return events;
    }
}
