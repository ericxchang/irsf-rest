package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.PartitionExportHistory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by echang on 3/14/2017.
 */
public interface PartitionExportHistoryRepository extends CrudRepository<PartitionExportHistory, Integer>{
 	@Query("select new PartitionExportHistory(pe.id, pe.partitionId, pe.origPartitionId, pe.exportDate, pe.midDataLoadTime,pe.exportFileLongSize,pe.exportFileShortSize,pe.exportWhitelistSize, pe.status, pe.reason) from PartitionExportHistory pe where pe.origPartitionId=?1 order by id desc")
	List<PartitionExportHistory> findAllByOrigPartitionId(int origPartitionId);
	
	PartitionExportHistory findOneByPartitionId(int partitionId);

	@Query("select pe.exportFileLong from PartitionExportHistory pe where pe.id=?1")
	byte[] findPartitonExportFullSet(Integer exportPartitionId);

    @Query("select distinct pe.origPartitionId from PartitionExportHistory pe")
	List<Integer> findAllOrigPartitionId();

    @Query("select pe.id from PartitionExportHistory pe where pe.origPartitionId=?1 order by id desc")
    List<Integer> findAllIdByOrigPartitionId(int origPartitionId);

    @Modifying
    @Transactional
    void deleteByIdIn(List<Integer> exportIds);
}
