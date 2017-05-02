package com.iconectiv.irsf.portal.repositories.common;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iconectiv.irsf.portal.model.common.ProviderBillingId;

public interface ProviderBillingIdRepository extends CrudRepository<ProviderBillingId, Integer>{

	List<ProviderBillingId> findByBillingId();
	List<ProviderBillingId> findByProvider();
}
