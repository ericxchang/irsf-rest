package com.iconectiv.irsf.portal.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.iconectiv.irsf.portal.core.EventTypeDefinition;
import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.common.EventNotificationRepository;
import com.iconectiv.irsf.portal.service.MobileIdDataService;
import com.iconectiv.irsf.portal.service.ScheduleJobService;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {
	private static Logger log = LoggerFactory.getLogger(ScheduleJobServiceImpl.class);
	
	private static Date lastUpdatedDate = null;
	
	@Autowired
	private EventNotificationRepository eventRepo;
	@Autowired
	private MobileIdDataService mobileIdService;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private CustomerDefinitionRepository customerRepo;
	
	
	@Override
	@Scheduled(cron = "0 6 0 ? * *")
	public void checkNewMobileIdUpdate() {
		EventNotification event = eventRepo.findTop1ByEventTypeOrderByCreateTimestampDesc(EventTypeDefinition.MobileIdUpdate.value());
		
		if (lastUpdatedDate == null || lastUpdatedDate.compareTo(event.getCreateTimestamp()) < 0) {
			lastUpdatedDate = event.getCreateTimestamp();

			if (log.isDebugEnabled()) log.debug("Sending event through web socket....");
			//messagingTemplate.convertAndSend("/topic/dataSetUpdateEvent", event);
			
			handleMobileIdDataReloadEvent();
		}
		
		return;

	}

	@CacheEvict(cacheNames = {"ccNDC", "country", "tos", "providers"}, allEntries = true)
	private void handleMobileIdDataReloadEvent() {
		log.info("Clear cached data ....");
		mobileIdService.cleanCache();
		
		/* TODO 
		 * 1. update active rule, update provider info
		 * 2. mark current partition stale
		 * 
		 */
		
	}

	
	@Override
	@Scheduled(cron = "1 * * * * *")
	public void partitionStaleNotify() {
		Iterable<CustomerDefinition> customers = customerRepo.findAll();
		
		for (CustomerDefinition customer: customers) {
			if (log.isDebugEnabled()) log.debug("sending partition stale event for customer {}", customer.getId());
			EventNotification event = new EventNotification();
			event.setCustomerName(customer.getCustomerName());
			event.setId(1);
			event.setMessage("found stale partition");
			messagingTemplate.convertAndSend("/topic/partitionStaleEvent." + customer.getId(), event);
		}
		
		return;

	}	
}