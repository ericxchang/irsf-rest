package com.iconectiv.irsf.portal.repositories.customer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.model.customer.PartitionDataDetails;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDataDetailRepositoryCustomer;

public class PartitionDataDetailsRepositoryImpl implements PartitionDataDetailRepositoryCustomer {
	private static Logger log = LoggerFactory.getLogger(PartitionDataDetailsRepositoryImpl.class);

	@Autowired
	EntityManagerFactory customerEntityManagerFactory;

	@Value("${jdbc.batch_size:500}")
	private int batchSize;
	 
	@Override
	@Transactional
	public void batchUpdate(Collection<PartitionDataDetails> entities) {
		final List<PartitionDataDetails> savedEntities = new ArrayList<>(entities.size());
		EntityManager entityManager = customerEntityManagerFactory.createEntityManager();
		entityManager.joinTransaction();
		
		int i = 0;
		for (PartitionDataDetails entity : entities) {
			savedEntities.add(persistOrMerge(entityManager, entity));
			i++;
			if (i % batchSize == 0) {
				if (log.isDebugEnabled()) log.debug("Flush ONE batch " + batchSize);
				entityManager.flush();
				entityManager.clear();
			}
		}
		entityManager.flush();
		entityManager.clear();

		log.info("Completed partition data list batch insert");
		return;
	}

	private PartitionDataDetails persistOrMerge(EntityManager entityManager, PartitionDataDetails entity) {
		if (entity.getId() == null) {
			entityManager.persist(entity);
			return entity;
		} else {
			return entityManager.merge(entity);
		}
	}


}