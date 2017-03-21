package com.iconectiv.irsf.portal.repositories.customer.impl;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
		Query query =  customerEntityManagerFactory.createEntityManager().createNamedStoredProcedureQuery("joinRange");
		query.setParameter("list_id", listId);
		query.setParameter("row_start",  startId);
		query.setParameter("num_recs", size);
		return query.getResultList();
	}

}
