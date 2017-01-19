package com.iconectiv.irsf.portal.repositories.common;

import com.iconectiv.irsf.portal.model.common.ListUploadRequest;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by echang on 1/12/2017.
 */
public interface ListUploadRequestRepository extends CrudRepository<ListUploadRequest, Integer>{
	List<ListUploadRequest> findAllByType(String type);
}
