package com.iconectiv.irsf.portal.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.iconectiv.irsf.portal.core.EventTypeDefinition;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.repositories.common.EventNotificationRepository;
import com.iconectiv.irsf.portal.service.ScheduleJobService;

@Service
public class ScheduleJpbServiceImpl implements ScheduleJobService {
	private static Logger log = LoggerFactory.getLogger(ScheduleJpbServiceImpl.class);
	
	private static Date lastUpdatedDate = null;
	
	@Autowired
	private EventNotificationRepository eventRepo;
	
	@Override
	@Scheduled(cron = "0 6 0 ? * *")
	public void checkNewMobileIdUpdate() {
		EventNotification event = eventRepo.findTop1ByEventTypeOrderByCreateTimestampDesc(EventTypeDefinition.MobileIdUpdate.value());
		
		if (lastUpdatedDate == null || lastUpdatedDate.compareTo(event.getCreateTimestamp()) < 0) {
			lastUpdatedDate = event.getCreateTimestamp();
			
			handleMobileIdDataReloadEvent();
		}
		
		return;

	}

	@CacheEvict(cacheNames = {"ccNDC", "country"}, allEntries = true)
	private void handleMobileIdDataReloadEvent() {
		log.info("Clear cached data ....");
	}

}
