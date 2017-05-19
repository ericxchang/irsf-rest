package com.iconectiv.irsf.portal.service;

import java.util.Date;
import java.util.List;

import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.model.common.UserDefinition;

public interface EventNotificationService {
	void addEventNotification(EventNotification event);
	void ackEventNotification(EventNotification event, UserDefinition user);
	List<EventNotification> getEvents(UserDefinition loginUser, Date lastQueryTime);
	List<EventNotification> getEvents();
	
	void broadcastPartitionEvent(Integer customerId, EventNotification event);
}
