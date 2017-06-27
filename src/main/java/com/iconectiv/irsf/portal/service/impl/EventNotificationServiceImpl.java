package com.iconectiv.irsf.portal.service.impl;

import com.iconectiv.irsf.portal.core.EventTypeDefinition;
import com.iconectiv.irsf.portal.model.common.AuditTrail;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.repositories.common.AuditTrailRepository;
import com.iconectiv.irsf.portal.repositories.common.EventNotificationRepository;
import com.iconectiv.irsf.portal.service.EventNotificationService;
import com.iconectiv.irsf.util.DateTimeHelper;
import com.iconectiv.irsf.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EventNotificationServiceImpl implements EventNotificationService {
	private static Logger log = LoggerFactory.getLogger(EventNotificationServiceImpl.class);
	
	@Autowired
	private EventNotificationRepository eventRepo;
	@Autowired
	private AuditTrailRepository auditRepo;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Override
	public void addEventNotification(EventNotification event) {
		eventRepo.save(event);
	}

	@Override
	public void sendPartitionEvent(UserDefinition loginUser, Integer partitionId, String type, String message) {
		EventNotification event = new EventNotification();
		event.setCreateTimestamp(DateTimeHelper.nowInUTC());
		event.setEventType(type);
		event.setSeverity(1);
		event.setReferenceId(partitionId);
		event.setCustomerName(loginUser.getCustomerName());
		event.setStatus("new");
		event.setMessage(message);
		sendPartitionEvent(loginUser, event);
	}

    @Override
    public void sendPartitionEvent(UserDefinition loginUser, EventNotification event) {
        event.setLastUpdatedBy(loginUser.getUserName());
        eventRepo.save(event);

        log.info(JsonHelper.toPrettyJson(event));
        messagingTemplate.convertAndSend("/topic/partitionEvent." + loginUser.getCustomerId(), event);
    }



    @Override
	public void ackEventNotification(EventNotification event, UserDefinition user) {
		event.setAcknowledgeTimestamp(DateTimeHelper.nowInUTC());
		event.setLastUpdatedBy(user.getUserName());
        event.setStatus("ack");
        eventRepo.save(event);
	}

	@Override
	public List<EventNotification> getEvents(UserDefinition loginUser, Date lastQueryTime) {
		List<EventNotification> events = new ArrayList<>();

		if (lastQueryTime == null) {
			//always return last mobile id refresh event
			events.add(eventRepo.findTop1ByEventTypeOrderByCreateTimestampDesc(EventTypeDefinition.MobileIdUpdate.value()));

			AuditTrail logoutEvent = auditRepo.findTop1ByUserNameAndActionOrderByLastUpdatedDesc(loginUser.getUserName(), "logout");
			
			if (logoutEvent != null) {
				lastQueryTime = logoutEvent.getLastUpdated();
			} else {
				lastQueryTime = DateTimeHelper.nowInUTC();
			}
		}

		if (log.isDebugEnabled()) log.debug("Query events after " + DateTimeHelper.formatDate( lastQueryTime, "yyyy-MM-dd HH:mm:ss z"));
		if (loginUser.getCustomerName() != null) {
			events.addAll(eventRepo.findAllByCustomerNameAndCreateTimestampGreaterThanOrderByCreateTimestampDesc(loginUser.getCustomerName(), lastQueryTime));
		}
		
		events.addAll(eventRepo.findAllByCustomerNameAndCreateTimestampGreaterThanOrderByCreateTimestampDesc("irsf", lastQueryTime));
		
		return events;
	}
}
