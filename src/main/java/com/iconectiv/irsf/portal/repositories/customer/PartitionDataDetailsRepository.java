package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.PartitionDataDetails;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by echang on 3/14/2017.
 */

public interface PartitionDataDetailsRepository extends CrudRepository<PartitionDataDetails, Integer>, PartitionDataDetailsRepositoryCustomer{
    
	List<PartitionDataDetails> findAllByPartitionId(Integer partitionid);
	
	@Query("select distinct dialPattern from PartitionDataDetails pd where pd.partitionId = ?1 and dataType in ?2 ")
	List<String> findDistinctDialPatternByPrtitionId(Integer partitionId, List<String> dataTypeList);
	
	@Modifying
    @Transactional
    @Query("update PartitionDataDetails pd set pd.partitionId=?2 where pd.partitionId=?1")
    void movePartition(Integer oldId, Integer newId);

    @Modifying
    @Transactional
    void deleteByPartitionId(Integer partitionid);
}
