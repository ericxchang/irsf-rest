package com.iconectiv.irsf.portal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.model.common.ListUploadRequest;
import com.iconectiv.irsf.portal.model.customer.BlackList;
import com.iconectiv.irsf.portal.repositories.common.ListUploadRequestRepository;
import com.iconectiv.irsf.portal.repositories.customer.BlackListRepository;
import com.iconectiv.irsf.portal.service.ListService;
import com.iconectiv.irsf.portal.util.JsonHelper;

/**
 * Created by echang on 1/11/2017.
 */
@Service
@Transactional
public class ListServiceImpl implements ListService {
    private static Logger log = LoggerFactory.getLogger(ListServiceImpl.class);
    
    @Autowired
    private BlackListRepository blackListRepo;
    @Autowired
    private ListUploadRequestRepository listUploadRepo;
    
    @Override
    @Async
    public void parseBlackList(ListUploadRequest uploadRequest) {
        log.info("Start parsing black list file {}", uploadRequest.getId());
        try {
        	CustomerContextHolder.setCustomer(uploadRequest.getAccount());
        	blackListRepo.deleteAllByCustomerIdAndListName(uploadRequest.getAccount(), uploadRequest.getListName());
        	List<BlackList> items = new ArrayList<BlackList>();
        	IntStream.range(1, 305).forEach(counter -> {
                BlackList item = new BlackList();
                item.setCustomerId(uploadRequest.getAccount());
                item.setListName(uploadRequest.getListName());
                item.setPhone(String.valueOf(counter));
                item.setLastUpdated(new Date());
                items.add(item);
                
                if (items.size() % 100 == 0) {
                    blackListRepo.save(items);
                	items.clear();
                }
        	});
        	
        	if (!items.isEmpty()) {
        		blackListRepo.save(items);
            	items.clear();
        	}
            uploadRequest.setStatus("complete");
            listUploadRepo.save(uploadRequest);
            log.info("Complete parsing list file {}", JsonHelper.toJson(uploadRequest));
        } catch (Exception e) {
            log.error("Error to parse black list: \n", e);
        }

    }

	@Override
	public ListUploadRequest saveUploadRequest(String customer, String listName, String filePath) {
        ListUploadRequest request = new ListUploadRequest();
        request.setAccount(customer);
        request.setListName(listName);
        request.setPath(filePath);
        request.setStatus("ready");
        request.setLastUpdated(new Date());
        listUploadRepo.save(request);
        
        log.debug("Save request {}", request.getId());
		return request;
	}
}
