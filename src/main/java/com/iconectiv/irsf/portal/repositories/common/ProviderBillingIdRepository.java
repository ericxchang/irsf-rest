package com.iconectiv.irsf.portal.repositories.common;

import com.iconectiv.irsf.portal.model.common.ProviderBillingId;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProviderBillingIdRepository extends CrudRepository<ProviderBillingId, Integer>{
	List<ProviderBillingId> findByBillingId(String billingId);
	List<ProviderBillingId> findByProvider(String provider);

    @Cacheable("providers")
    @Query("select new ProviderBillingId(r.provider) from ProviderBillingId r group by r.provider")
	List<ProviderBillingId> findAllGroupByProvider();
}
