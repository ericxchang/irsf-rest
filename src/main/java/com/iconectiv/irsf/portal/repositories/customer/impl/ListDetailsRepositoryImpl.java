package com.iconectiv.irsf.portal.repositories.customer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.repositories.customer.ListDetailsRepositoryCustomer;

public class ListDetailsRepositoryImpl implements ListDetailsRepositoryCustomer {
	private static Logger log = LoggerFactory.getLogger(ListDetailsRepositoryImpl.class);

	@Autowired
	EntityManagerFactory customerEntityManagerFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<ListDetails> queryJoinByListRefId(int listId) {
		log.info("Call stored procedure ...");
		Query query = customerEntityManagerFactory.createEntityManager().createNamedStoredProcedureQuery("joinAll");
		query.setParameter("list_id", listId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ListDetails> queryJoinByListRefId(int listId, int startId, int size) {
		log.info("Call stored procedure ...");
		Query query = customerEntityManagerFactory.createEntityManager().createNamedStoredProcedureQuery("joinRange");
		query.setParameter("list_id", listId);
		query.setParameter("row_start", startId);
		query.setParameter("num_recs", size);
		return query.getResultList();
	}

	@Value("${hibernate.jdbc.batch_size}")
	private int batchSize;
	 
	@Override
	@Transactional
	public void batchUpdate(Collection<ListDetails> entities) {
		final List<ListDetails> savedEntities = new ArrayList<>(entities.size());
		EntityManager entityManager = customerEntityManagerFactory.createEntityManager();
		entityManager.joinTransaction();
		
		int i = 0;
		for (ListDetails entity : entities) {
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

		if (log.isDebugEnabled()) log.debug("Completed batch insert");
		return;
	}

	private ListDetails persistOrMerge(EntityManager entityManager, ListDetails entity) {
		if (entity.getId() == null) {
			entityManager.persist(entity);
			return entity;
		} else {
			return entityManager.merge(entity);
		}
	}

}
