package com.iconectiv.irsf.portal.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.repositories.common.EventNotificationRepository;
import com.iconectiv.irsf.portal.service.EventNotificationService;

@Service
public class EventNotificationServiceImpl implements EventNotificationService {
	private static Logger log = LoggerFactory.getLogger(EventNotificationServiceImpl.class);
	
	@Autowired
	private EventNotificationRepository eventRepo;
	
	@Override
	public void addEventNotification(EventNotification event) {
		eventRepo.save(event);

	}

	@Override
	public void ackEventNotification(EventNotification event, UserDefinition user) {
		// TODO Auto-generated method stub

	}

}
