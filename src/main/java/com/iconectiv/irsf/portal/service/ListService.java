package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;

import java.util.List;

/**
 * Created by echang on 1/11/2017.
 */
public interface ListService {
	void processListUploadRequest(UserDefinition user, ListDefinition listDef, ListUploadRequest uploadReq, boolean isInitialLoading);

	void deleteListDefinition(UserDefinition loginUser, int listId);
	void deleteListDefinition(UserDefinition loginUser, String listName);

	ListDefinition getListDetails(int listId);
	ListDefinition getListDetails(String listName);

	List<ListDetails> getListDetailDataByListId(int listId);

	List<ListDetails> getListDetailDataByUploadId(int uploadId);

	List<ListDefinition> getTop3ListDefinition(String listType);

	void updateListDetails(UserDefinition loginUser, ListDetails[] listDetails) throws AppException;

	void deleteListDetails(UserDefinition loginUser, ListDetails[] listDetails) throws AppException;

	Iterable<ListDetails> createListDetails(UserDefinition loginUser, ListDetails[] listDetails) throws AppException;

	void getListDetailDataByDialPattern(ListDetails listDetail);

	void createListDefinition(UserDefinition loginUser, ListDefinition listDef);
}
