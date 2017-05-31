package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.model.common.UserDefinition;

import java.util.Date;
import java.util.List;

public interface EventNotificationService {
	void addEventNotification(EventNotification event);

    void sendPartitionEvent(UserDefinition loginUser, EventNotification event);

    void ackEventNotification(EventNotification event, UserDefinition user);
	List<EventNotification> getEvents(UserDefinition loginUser, Date lastQueryTime);
	List<EventNotification> getEvents();

	void sendPartitionEvent(UserDefinition loginUser, Integer partitionId, String eventType, String message);
}
