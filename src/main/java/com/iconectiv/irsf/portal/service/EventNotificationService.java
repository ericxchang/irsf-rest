package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.model.common.UserDefinition;

public interface EventNotificationService {
	void addEventNotification(EventNotification event);
	void ackEventNotification(EventNotification event, UserDefinition user);
}
