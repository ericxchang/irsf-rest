package com.iconectiv.irsf.portal.repositories.customer;

import java.util.Collection;

import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.customer.PartitionDataDetails;

public interface PartitionDataDetailsRepositoryCustomer {
	 void batchUpdate(Collection<PartitionDataDetails> items) throws AppException;
}  