package com.iconectiv.irsf.portal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.AuditTrail;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.repositories.common.AuditTrailRepository;
import com.iconectiv.irsf.portal.repositories.common.EventNotificationRepository;
import com.iconectiv.irsf.portal.service.EventNotificationService;

@Service
public class EventNotificationServiceImpl implements EventNotificationService {
	private static Logger log = LoggerFactory.getLogger(EventNotificationServiceImpl.class);
	
	@Autowired
	private EventNotificationRepository eventRepo;
	@Autowired
	private AuditTrailRepository auditRepo;
	
	@Override
	public void addEventNotification(EventNotification event) {
		eventRepo.save(event);

	}

	@Override
	public void ackEventNotification(EventNotification event, UserDefinition user) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<EventNotification> getEvents(UserDefinition loginUser, Date lastQueryTime) {
		List<EventNotification> events = new ArrayList<>();
		if (lastQueryTime == null) {
			AuditTrail logoutEvent = auditRepo.findTop1ByUserNameAndActionOrderByLastUpdatedDesc(loginUser.getUserName(), "logout");
			
			if (logoutEvent != null) {
				lastQueryTime = logoutEvent.getLastUpdated();
			} else {
				lastQueryTime = new Date();
			}
		}
		
		//TODO convert time to GMT
		
		if (loginUser.getCustomerName() != null) {
			events.addAll(eventRepo.findAllByCustomerNameAndCreateTimestampGreaterThanOrderByCreateTimestampDesc(loginUser.getCustomerName(), lastQueryTime));
		}
		
		events.addAll(eventRepo.findAllByCustomerNameAndCreateTimestampGreaterThanOrderByCreateTimestampDesc("IRSF", lastQueryTime));
		
		return events;
	}

}
