package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.model.customer.ListDefintion;
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by echang on 1/11/2017.
 */
public interface ListService {

	Integer createListDefinition(String customer, String listName, String listType, String user);

	ListUploadRequest saveUploadRequest(String customer, ListDefintion listDef, MultipartFile file, String delimiter, String user);

	void processListUploadRequest(ListUploadRequest uploadRequest);

	void deleteListDefinition(String customer, String listName);

	ListDefintion getListDetails(String customer, String listName);
}
