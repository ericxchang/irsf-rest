package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.ListDefintion;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by echang on 1/12/2017.
 */
public interface ListDefinitionRepository extends CrudRepository<ListDefintion, Integer>{
    ListDefintion findOneByListName(String listName);

    List<ListDefintion> findTop3ByTypeAndActiveOrderByLastUpdatedDesc(String type, boolean active);
    
    
    @Modifying
    @Transactional
    void deleteByListName(String listName);
}
