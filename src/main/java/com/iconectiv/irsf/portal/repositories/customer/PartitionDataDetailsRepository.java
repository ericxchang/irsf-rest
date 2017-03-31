package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.PartitionDataDetails;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by echang on 3/14/2017.
 */
public interface PartitionDataDetailsRepository extends CrudRepository<PartitionDataDetails, Integer>{
    @Modifying
    @Transactional
    @Query("update PartitionDataDetails pd set pd.partitionId=?2 where pd.partitionId=?1")
    void movePartition(Integer oldId, Integer newId);

    @Modifying
    @Transactional
    void deleteByPartitionId(Integer partitionid);
}
