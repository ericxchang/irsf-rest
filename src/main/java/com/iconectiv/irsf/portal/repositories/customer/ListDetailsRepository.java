package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.ListDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by echang on 1/12/2017.
 */
public interface ListDetailsRepository extends CrudRepository<ListDetails, Integer>, ListDetailsRepositoryCustomer {
    ListDetails findOneByListRefIdAndDialPattern(int lstRefId, String dialPattern);
    List<ListDetails> findAllByUpLoadRefId(int uploadRefId);
    List<ListDetails> findAllByListRefId(int listRefId);

    @Query(value="select ld.id, ld.list_ref_id, ld.upload_req_ref_id, ld.dial_pattern, ld.reason, ld.notes, ld.customer_date, ld.active, ld.match_cc_ndc, ld.last_updated, ld.last_updated_by,"
    		+ " rt.term_country, rt.cc_ndc, rt.iso2, rt.code, rt.tos, rt.tosdesc, rt.ndc, rt.locality, rt.provider, rt.billing_id,rt.supplement, rt.effective_date"
    		+ " from list_details ld LEFT JOIN irsfmast.range_ndc rt on rt.cc_ndc=ld.match_cc_ndc where ld.upload_req_ref_id=?1", nativeQuery = true)
    List<Object[]> findAllDetailsByUpLoadRefId(int uploadRefId);

    @Query(value="select ld.id, ld.list_ref_id, ld.upload_req_ref_id, ld.dial_pattern, ld.reason, ld.notes, ld.customer_date, ld.active, ld.match_cc_ndc, ld.last_updated, ld.last_updated_by,"
    		+ " rt.term_country, rt.cc_ndc, rt.iso2, rt.code, rt.tos, rt.tosdesc, rt.ndc, rt.locality, rt.provider, rt.billing_id,rt.supplement, rt.effective_date"
    		+ " from list_details ld LEFT JOIN irsfmast.range_ndc rt on rt.cc_ndc=ld.match_cc_ndc where ld.list_ref_id=?1", nativeQuery = true)
    List<Object[]> findAllDetailsByListRefId(int listRefId);
}
