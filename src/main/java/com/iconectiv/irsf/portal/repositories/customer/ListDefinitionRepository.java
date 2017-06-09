package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.ListDefinition;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by echang on 1/12/2017.
 */
public interface ListDefinitionRepository extends CrudRepository<ListDefinition, Integer>{
    ListDefinition findOneByListName(String listName);

    List<ListDefinition> findTop3ByActive(boolean active);

    List<ListDefinition> findTop3ByTypeAndActiveOrderByLastUpdatedDesc(String type, boolean active);   

    
    
    @Modifying
    @Transactional
    void deleteByListName(String listName);

}

	
