package com.iconectiv.irsf.portal.repositories.customer.impl;

import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.repositories.customer.ListDetailsRepositoryCustomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListDetailsRepositoryImpl implements ListDetailsRepositoryCustomer {
	private static Logger log = LoggerFactory.getLogger(ListDetailsRepositoryImpl.class);

	@Autowired
	EntityManagerFactory customerEntityManagerFactory;

/*	@SuppressWarnings("unchecked")
	@Override
	public List<ListDetails> queryJoinByListRefId(int listId) {
		log.info("Call stored procedure ...");
		Query query = customerEntityManagerFactory.createEntityManager().createNamedStoredProcedureQuery("joinAll");
		query.setParameter("list_id", listId);
		return query.getResultList();
	}
*/
	@Value("${jdbc.batch_size:500}")
	private int batchSize;
	 
	@Override
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

		log.info("Completed list batch insert");
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
