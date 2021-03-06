package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by echang on 1/12/2017.
 */
public interface ListUploadRequestRepository extends CrudRepository<ListUploadRequest, Integer>{
	List<ListUploadRequest> findAllByListRefIdOrderByLastUpdatedDesc(Integer listRefId);
	ListUploadRequest findTop1ByListRefIdOrderByLastUpdatedDesc(Integer listRefId);
	
    @Modifying
    @Transactional	
	void deleteAllByListRefId(Integer id);
}
