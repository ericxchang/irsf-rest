package com.iconectiv.irsf.portal.repositories.customer;

import java.util.Collection;

import com.iconectiv.irsf.portal.model.customer.PartitionDataDetails;

public interface PartitionDataDetailRepositoryCustomer {
	 void batchUpdate(Collection<PartitionDataDetails> items);
}  