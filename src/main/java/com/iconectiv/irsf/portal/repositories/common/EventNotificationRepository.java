package com.iconectiv.irsf.portal.repositories.common;

import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.model.customer.ListDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by echang on 1/12/2017.
 */
public interface EventNotificationRepository extends CrudRepository<EventNotification, Integer>{
    List<EventNotification> findAllByCustomerName(String customerName);
}
