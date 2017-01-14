package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.model.common.ListUploadRequest;

import java.util.List;

/**
 * Created by echang on 1/11/2017.
 */
public interface ListService {

    void parseBlackList(ListUploadRequest request);
    void parseBlackList(ListUploadRequest request, List<String> contents);

	ListUploadRequest saveUploadRequest(String customer, String listName, String type, String filePath);

}
