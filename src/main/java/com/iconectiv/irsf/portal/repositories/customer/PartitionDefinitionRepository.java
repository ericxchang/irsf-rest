package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by echang on 3/14/2017.
 */
public interface PartitionDefinitionRepository extends CrudRepository<PartitionDefinition, Integer>{

    @Query("select pd from PartitionDefinition pd where pd.status <> 'locked'" )
    List<PartitionDefinition> findAllActivePartitions();
    
    List<PartitionDefinition> findAllByOrigPartitionIdOrderByIdDesc(Integer origPartitionId);
    
    @Modifying
    @Transactional
    @Query("update PartitionDefinition pd set pd.status='stale', pd.draftDate=null where pd.status='draft' and pd.ruleIds like %?1%")
    void staleDraftPartitionsByRuleId(Integer listId);

    @Modifying
    @Transactional
    @Query("update PartitionDefinition pd set pd.status='stale', pd.draftDate=null where pd.status='draft' and pd.blId=?1")
    void staleDraftPartitionsByBListId(Integer listId);

    @Modifying
    @Transactional
    @Query("update PartitionDefinition pd set pd.status='stale', pd.draftDate=null where pd.status='draft' and pd.wlId=?1")
    void staleDraftPartitionsByWListId(Integer listId);
}
