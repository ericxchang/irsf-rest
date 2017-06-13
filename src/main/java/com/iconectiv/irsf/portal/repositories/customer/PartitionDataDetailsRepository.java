package com.iconectiv.irsf.portal.repositories.customer;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.iconectiv.irsf.portal.model.customer.PartitionDataDetails;
import com.iconectiv.irsf.portal.model.customer.PartitionSummary;

/**
 * Created by echang on 3/14/2017.
 */

public interface PartitionDataDetailsRepository extends CrudRepository<PartitionDataDetails, Integer>, PartitionDataDetailsRepositoryCustomer{
    
	List<PartitionDataDetails> findAllByPartitionId(Integer partitionid);
	Page<PartitionDataDetails> findAllByPartitionId(Integer partitionId, Pageable pageable);
	
	@Query("select distinct dialPattern from PartitionDataDetails pd where pd.partitionId = ?1 and pd.dataType in ?2 ")
	List<String> findDistinctDialPatternByPrtitionId(Integer partitionId, List<String> dataTypeList);
	
	@Query("select new PartitionSummary(pd.dialPattern, pd.type) from PartitionDataDetails pd where pd.partitionId = ?1 and pd.dataType in ?2 group by pd.dialPattern, pd.type ")
	List<PartitionSummary> findDistinctDialPatternSummaryByPrtitionId(Integer partitionId, List<String> dataTypeList);
	
	@Modifying
    @Transactional
    @Query("update PartitionDataDetails pd set pd.partitionId=?2 where pd.partitionId=?1")
    void movePartition(Integer oldId, Integer newId);

    @Modifying
    @Transactional
    void deleteByPartitionId(Integer partitionid);

}
