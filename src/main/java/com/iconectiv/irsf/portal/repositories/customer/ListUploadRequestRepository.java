package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by echang on 1/12/2017.
 */
public interface ListUploadRequestRepository extends CrudRepository<ListUploadRequest, Integer>{
	List<ListUploadRequest> findAllByListRefId(Integer listRefId);
}
