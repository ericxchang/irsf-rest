package com.iconectiv.irsf.portal.repositories.common;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iconectiv.irsf.portal.model.common.ProviderBillingIdPrior;

public interface ProviderBillingIdPriorRepository extends CrudRepository<ProviderBillingIdPrior, Integer>{

	List<ProviderBillingIdPrior> findByBillingId();
	List<ProviderBillingIdPrior> findByProvider();
}
