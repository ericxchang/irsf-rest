package com.iconectiv.irsf.portal.service.impl;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.EventTypeDefinition;
import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.common.EventNotificationRepository;
import com.iconectiv.irsf.portal.service.MobileIdDataService;
import com.iconectiv.irsf.portal.service.PartitionExportService;
import com.iconectiv.irsf.portal.service.PartitionService;
import com.iconectiv.irsf.portal.service.ScheduleJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

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
	@Autowired
    private PartitionService partitionService;
	@Autowired
    private PartitionExportService exportService;
	
	
	@Override
	@Scheduled(cron = "0 6 0 ? * *")
	public void checkNewMobileIdUpdate() {
		EventNotification event = eventRepo.findTop1ByEventTypeOrderByCreateTimestampDesc(EventTypeDefinition.MobileIdUpdate.value());

		if (event == null) {
		    //should not happen, but add protection anyway
            return;
        }

		if (lastUpdatedDate == null || lastUpdatedDate.compareTo(event.getCreateTimestamp()) < 0) {
			lastUpdatedDate = event.getCreateTimestamp();

			if (log.isDebugEnabled()) log.debug("Sending event through web socket....");
			messagingTemplate.convertAndSend("/topic/dataSetUpdateEvent", event);
			
			handleMobileIdDataReloadEvent(lastUpdatedDate);
		}
		
		return;

	}

	@CacheEvict(cacheNames = {"ccNDC", "country", "tos", "providers"}, allEntries = true)
	public void handleMobileIdDataReloadEvent(Date lastUpdatedDate) {
		log.info("Clear cached data ....");
		mobileIdService.cleanCache();

		checkPartitionState(lastUpdatedDate);

		cleanExportHistory();
		/* TODO 
		 * 1. update active rule, update provider info
		 * 2. mark current partition stale
		 * 
		 */
	}

    private void cleanExportHistory() {
        for (CustomerDefinition customer: customerRepo.findAllByActive(true)) {
            log.info("Scanning draft partition for customer {}" + customer.getCustomerName());
            CustomerContextHolder.setSchema(customer.getSchemaName());
            exportService.cleanExportHistory(customer);
        }
    }

    private void checkPartitionState(Date lastUpdatedDate) {
	    for (CustomerDefinition customer: customerRepo.findAllByActive(true)) {
	        log.info("Scanning draft partition for customer {}" + customer.getCustomerName());
            CustomerContextHolder.setSchema(customer.getSchemaName());
            partitionService.staleDraftPartitions(customer, lastUpdatedDate);
        }
    }

}
