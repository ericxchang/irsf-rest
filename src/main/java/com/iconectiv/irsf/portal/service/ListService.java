package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.model.common.ListUploadRequest;

/**
 * Created by echang on 1/11/2017.
 */
public interface ListService {

    void parseBlackList(ListUploadRequest request);

	ListUploadRequest saveUploadRequest(String customer, String listName, String filePath);
}
