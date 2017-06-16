package com.iconectiv.irsf.portal.repositories.customer.impl;

import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.customer.PartitionDataDetails;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDataDetailsRepositoryCustomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PartitionDataDetailsRepositoryImpl implements PartitionDataDetailsRepositoryCustomer {
	private static Logger log = LoggerFactory.getLogger(PartitionDataDetailsRepositoryImpl.class);

	@Autowired
	EntityManagerFactory customerEntityManagerFactory;

	@Value("${jdbc.batch_size:500}")
	private int batchSize;
	 
	@Override
	@Transactional
	public void batchUpdate(Collection<PartitionDataDetails> entities) throws AppException {
		if (log.isTraceEnabled())log.debug("batchUpdate {} rows ", entities.size());
		if (entities == null || entities.isEmpty()) {
			log.debug("batchUpdate(): no row found");
			return;
		}
		final List<PartitionDataDetails> savedEntities = new ArrayList<>(entities.size());
		EntityManager entityManager = customerEntityManagerFactory.createEntityManager();
		entityManager.joinTransaction();
		
		int i = 0;
		int batchCount = 0;
		if (log.isDebugEnabled()) log.debug("batchUpdate(): insert {} rows", batchSize, entities.size());
		for (PartitionDataDetails entity : entities) {
			try {
				savedEntities.add(persistOrMerge(entityManager, entity));
				i++;
				if (i % batchSize == 0) {
					batchCount++;
					if (log.isDebugEnabled()) log.debug("Flush ONE batch {}, total: {}, batch number: {}", batchSize, batchCount*batchSize, batchCount);
					if (batchCount % 100 == 0) {
						log.debug("batchUpdate(): Total Memory: {} KB, Free Memory: {} KB ", (double) Runtime.getRuntime().totalMemory()/1024,  (double) Runtime.getRuntime().freeMemory()/ 1024);
					}
					entityManager.flush();
					entityManager.clear();
				}
			} catch (Exception e) {
				log.error("batchUpdate failed: {}, Total Memory: {} KB, Free Memory: {} KB ", e.getMessage(), (double) Runtime.getRuntime().totalMemory()/1024,  (double) Runtime.getRuntime().freeMemory()/ 1024);
				log.debug("last partition date: {}, number of rows insert so far: {}", entity.toCSVString("|"), savedEntities.size());
				entityManager = customerEntityManagerFactory.createEntityManager();
				entityManager.joinTransaction();
				//throw new AppException(e.getMessage());
			}
		}
		try {
			entityManager.flush();
			entityManager.clear();
		} catch (Exception e) {
			log.error("batchUpdate failed - {}, Total Memory: {} KB, Free Memory: {} KB ", e.getMessage(), (double) Runtime.getRuntime().totalMemory()/1024,  (double) Runtime.getRuntime().freeMemory()/ 1024);
			throw new AppException(e.getMessage());
		}

		log.info("Completed partition data list batch insert. Number of rows inserted: {}", savedEntities.size());
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