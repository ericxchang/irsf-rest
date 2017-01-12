package com.iconectiv.irsf.portal.service.impl;

import com.iconectiv.irsf.portal.service.ListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by echang on 1/11/2017.
 */
@Service
public class ListServiceImpl implements ListService {
    private static Logger log = LoggerFactory.getLogger(ListServiceImpl.class);
    @Override
    @Async
    public void parseBlackList(int id) {
        log.info("Start parsing black list file {}", id);
        try {
            Thread.sleep(20*1000);
            log.info("Complete parsing black list list");
        } catch (InterruptedException e) {
            //ignore
        }

    }
}
