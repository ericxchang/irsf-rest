package com.iconectiv.irsf.portal.repositories.common;

import com.iconectiv.irsf.portal.model.common.EventNotification;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by echang on 1/12/2017.
 */
public interface EventNotificationRepository extends CrudRepository<EventNotification, Integer>{
    List<EventNotification> findAllByCustomerName(String customerName);
    List<EventNotification> findAllByCustomerNameAndEventType(String customerName, String eventType);
    
    List<EventNotification> findAllByCustomerNameIsNullAndCreateTimestampGreaterThanOrderByCreateTimestampDesc(Date createTimestamp);

    List<EventNotification> findAllByCustomerNameAndCreateTimestampGreaterThanOrderByCreateTimestampDesc(String customerName, Date createTimestamp);
    List<EventNotification> findAllByCustomerNameAndEventTypeAndCreateTimestampGreaterThanOrderByCreateTimestampDesc(String customerName, String eventType, Date createTimestamp);

    EventNotification findTop1ByEventTypeOrderByCreateTimestampDesc(String eventType);
    EventNotification findTop1ByCustomerNameAndEventTypeOrderByCreateTimestampDesc(String customerName, String eventType);
}
