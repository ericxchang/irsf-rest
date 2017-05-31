package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.PartitionExportHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by echang on 3/14/2017.
 */
public interface PartitionExportHistoryRepository extends CrudRepository<PartitionExportHistory, Integer>{
 	@Query("select new PartitionExportHistory(pe.id, pe.partitionId, pe.origPartitionId, pe.exportDate, pe.midDataLoadTime,pe.exportFileLongSize,pe.exportFileShortSize,pe.exportWhitelistSize, pe.status, pe.reason) from PartitionExportHistory pe where pe.origPartitionId=?1 order by id desc")
	List<PartitionExportHistory> findAllByOrigPartitionId(int origPartitionId);
	
	@Query("select new PartitionExportHistory(pe.id, pe.partitionId, pe.origPartitionId, pe.exportDate, pe.midDataLoadTime,pe.exportFileLongSize,pe.exportFileShortSize,pe.exportWhitelistSize, pe.status, pe.reason) from PartitionExportHistory pe where pe.partitionId=?1 order by exportDate desc")
	List<PartitionExportHistory> findAllByPartitionId(int partitionId);

	@Query("select pe.exportFileLong from PartitionExportHistory pe where pe.id=?1")
	byte[] findPartitonExportFullSet(Integer exportPartitionId);
}
