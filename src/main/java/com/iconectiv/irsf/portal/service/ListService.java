package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefintion;
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by echang on 1/11/2017.
 */
public interface ListService {

	Integer createListDefinition(UserDefinition user, String listName, String listType);

	ListUploadRequest saveUploadRequest(UserDefinition user, ListDefintion listDef, MultipartFile file, String delimiter);

	void processListUploadRequest(ListUploadRequest uploadRequest, Boolean isInitialLoading);

	void deleteListDefinition(String listName);

	ListDefintion getListDetails(String listName);
}
