package com.iconectiv.irsf.portal.repositories.common;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iconectiv.irsf.portal.model.common.EventNotification;

/**
 * Created by echang on 1/12/2017.
 */
public interface EventNotificationRepository extends CrudRepository<EventNotification, Integer>{
    List<EventNotification> findAllByCustomerName(String customerName);
    List<EventNotification> findAllByCustomerNameAndEventType(String customerName, String eventType);
    
    List<EventNotification> findAllByCustomerNameAndCreateTimestampGreaterThanOrderByCreateTimestampDesc(String customerName, Date createTimestamp);
    List<EventNotification> findAllByCustomerNameAndEventTypeAndCreateTimestampGreaterThanOrderByCreateTimestampDesc(String customerName, String eventType, Date createTimestamp);

    EventNotification findTop1ByEventTypeOrderByCreateTimestampDesc(String eventType);
    EventNotification findTop1ByCustomerNameAndEventTypeOrderByCreateTimestampDesc(String customerName, String eventType);
}
