package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by echang on 3/14/2017.
 */
public interface PartitionDefinitionRepository extends CrudRepository<PartitionDefinition, Integer>{

    @Query("select pd from PartitionDefinition pd where pd.status <> 'locked'" )
    List<PartitionDefinition> findAllActivePartitions();
}
