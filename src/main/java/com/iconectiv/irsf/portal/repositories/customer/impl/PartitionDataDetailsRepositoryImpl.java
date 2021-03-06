package com.iconectiv.irsf.portal.repositories.customer.impl;

import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.customer.PartitionDataDetails;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDataDetailsRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDataDetailsRepositoryCustomer;

import org.hibernate.exception.GenericJDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PartitionDataDetailsRepositoryImpl implements PartitionDataDetailsRepositoryCustomer {
	private static Logger log = LoggerFactory.getLogger(PartitionDataDetailsRepositoryImpl.class);

	@Autowired
	EntityManagerFactory customerEntityManagerFactory;
	
	@Autowired
	PartitionDataDetailsRepository partpDetailsRepo;
	

	@Value("${jdbc.batch_size:500}")
	private int batchSize;
	 
	@Override
	@Transactional
	public void batchUpdate(Collection<PartitionDataDetails> entities) throws AppException {
		long begTime = System.currentTimeMillis();
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
		if (log.isDebugEnabled())log.debug("batchUpdate(): insert {} rows", batchSize, entities.size());
		
		for (PartitionDataDetails entity : entities) {

			try {
				savedEntities.add(persistOrMerge(entityManager, entity));
				i++;
				if (i % batchSize == 0) {
					batchCount++;
					if (log.isDebugEnabled()) log.debug("Flush ONE batch {}, total: {}, batch number: {}", batchSize, batchCount * batchSize, batchCount);
					if (batchCount % 100 == 0) {
						log.info("batchUpdate(): Total Memory: {} KB, Free Memory: {} KB ", (double) Runtime.getRuntime().totalMemory() / 1024,	(double) Runtime.getRuntime().freeMemory() / 1024);
					}
					entityManager.flush();
					entityManager.clear();
				}
			}

			catch (GenericJDBCException e) {
				log.error("GenericJDBCException:: JDBC ERROR: batchUpdate failed: {}, Total Memory: {} KB, Free Memory: {} KB ",
						e.getMessage(), (double) Runtime.getRuntime().totalMemory() / 1024,	(double) Runtime.getRuntime().freeMemory() / 1024);
				log.error("GenericJDBCException:: ErrorCode: {}, SQLState: {}, SQLException: {}, SQL: {}, message: {}",
						e.getErrorCode(), e.getSQLState(),	e.getSQLException() == null ? "No SQLException" : e.getMessage(), e.getSQL(), e.getMessage());
				if (entity != null)
					log.debug("GenericJDBCException:: last partition date: {}, number of rows insert so far: {}", entity.toCSVString("|"), i);
				else {
					log.debug("GenericJDBCException:: last partition datais is null, number of rows insert so far: {}", i);
				}
				throw new AppException(e.getMessage());
			} catch (Exception e) {
				log.error("batchUpdate failed: {}, Total Memory: {} KB, Free Memory: {} KB ", e.getMessage(), (double) Runtime.getRuntime().totalMemory() / 1024,	(double) Runtime.getRuntime().freeMemory() / 1024);
				if (entity != null)
					log.debug("last partition data: {}, number of rows insert so far: {}", entity.toCSVString("|"),	i);
				else {
					log.debug("last partition datais is null, number of rows insert so far: {}", i);
				}
				throw new AppException(e.getMessage());
			}

		}

		try {
			entityManager.flush();
			entityManager.clear();
		} catch (Exception e) {
			log.error("*batchUpdate failed - {}, Total Memory: {} KB, Free Memory: {} KB ", e.getMessage(),
					(double) Runtime.getRuntime().totalMemory() / 1024,	(double) Runtime.getRuntime().freeMemory() / 1024);
			throw new AppException(e.getMessage());
		}
	 

		log.info("Completed partition data list batch insert. Number of rows inserted: {}, duration: {} seconds", savedEntities.size(), (System.currentTimeMillis() - begTime)/1000.0);
			
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