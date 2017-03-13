package com.iconectiv.irsf.portal.repositories.common;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iconectiv.irsf.portal.model.common.EventNotification;

/**
 * Created by echang on 1/12/2017.
 */
public interface EventNotificationRepository extends CrudRepository<EventNotification, Integer>{
    List<EventNotification> findAllByCustomerName(String customerName);
}
