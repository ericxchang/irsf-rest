package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by echang on 1/11/2017.
 */
public interface ListService {

	Integer createListDefinition(UserDefinition user, String listName, String listType);

	ListUploadRequest saveUploadRequest(UserDefinition user, ListDefinition listDef, MultipartFile file, String delimiter);

	void processListUploadRequest(ListUploadRequest uploadRequest, Boolean isInitialLoading);

	void deleteListDefinition(int listId);
	void deleteListDefinition(String listName);

	ListDefinition getListDetails(int listId);
	ListDefinition getListDetails(String listName);

	List<ListDetails> getListDetailDataByListId(int listId);

	List<ListDetails> getListDetailDataByUploadId(int uploadId);

	List<ListDefinition> getTop3ListDefinition(String listType);

	void updateListName(UserDefinition loginUser, Integer listId, String listName);

	void updateListDetails(UserDefinition loginUser, ListDetails[] listDetails) throws AppException;

	void deleteListDetails(UserDefinition loginUser, ListDetails[] listDetails) throws AppException;

	Iterable<ListDetails> createListDetails(UserDefinition loginUser, ListDetails[] listDetails) throws AppException;

	void getListDetailDataByDialPattern(ListDetails listDetail);


}
