package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.ListDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by echang on 1/12/2017.
 */
public interface ListDetailsRepository extends CrudRepository<ListDetails, Integer>, ListDetailsRepositoryCustomer {
    ListDetails findOneByListRefIdAndDialPattern(int lstRefId, String dialPattern);
    List<ListDetails> findAllByUpLoadRefId(int uploadRefId);
}
