package com.iconectiv.irsf.portal.repositories.common;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.iconectiv.irsf.portal.model.common.RangeNdc;
import com.iconectiv.irsf.portal.repositories.ReadOnlyRepository;

/**
 * Created by echang on 1/12/2017.
 */
public interface RangeNdcRepository extends ReadOnlyRepository <RangeNdc, String>{
	@Cacheable("providers") 
	@Query("select r.billingId, r.provider from RangeNdc r where r.provider is not null group by r.provider, r.billingId")
	List<Object[]> findAllProviders();

	@Query("select p from RangeNdc p where (code in ?1 )")
	List<RangeNdc>  findRangeNdcbyRule1(List<String> codeList);

	@Query("select p from RangeNdc p where (iso2 in ?1 )")
	List<RangeNdc>  findRangeNdcbyRule2(List<String> iso2List);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 )")
	List<RangeNdc>  findRangeNdcbyRule3(List<String> codeList, List<String> iso2List);

	@Query("select p from RangeNdc p where (tos in ?1 )")
	List<RangeNdc>  findRangeNdcbyRule4(List<String> tosList);

	@Query("select p from RangeNdc p where (code in ?1 ) and (tos in ?2 )")
	List<RangeNdc>  findRangeNdcbyRule5(List<String> codeList, List<String> tosList);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and (tos in ?2 )")
	List<RangeNdc>  findRangeNdcbyRule6(List<String> iso2List, List<String> tosList);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 )")
	List<RangeNdc>  findRangeNdcbyRule7(List<String> codeList, List<String> iso2List, List<String> tosList);

	@Query("select p from RangeNdc p where (tosdesc in ?1 )")
	List<RangeNdc>  findRangeNdcbyRule8(List<String> tosDescList);

	@Query("select p from RangeNdc p where (code in ?1 ) and (tosdesc in ?2 )")
	List<RangeNdc>  findRangeNdcbyRule9(List<String> codeList, List<String> tosDescList);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and (tosdesc in ?2 )")
	List<RangeNdc>  findRangeNdcbyRule10(List<String> iso2List, List<String> tosDescList);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and (tosdesc in ?3 )")
	List<RangeNdc>  findRangeNdcbyRule11(List<String> codeList, List<String> iso2List, List<String> tosDescList);

	@Query("select p from RangeNdc p where tos in ?1 or concat(tos,',',tosdesc) in ?2 ")
	List<RangeNdc>  findRangeNdcbyRule12(List<String> tosList, List<String> tosDescList);

	@Query("select p from RangeNdc p where (code in ?1 ) and tos in ?2 or concat(tos,',',tosdesc) in ?3 ")
	List<RangeNdc>  findRangeNdcbyRule13(List<String> codeList, List<String> tosList, List<String> tosDescList);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and tos in ?2 or concat(tos,',',tosdesc) in ?3 ")
	List<RangeNdc>  findRangeNdcbyRule14(List<String> iso2List, List<String> tosList, List<String> tosDescList);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and tos in ?3 or concat(tos,',',tosdesc) in ?4 ")
	List<RangeNdc>  findRangeNdcbyRule15(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList);

	@Query("select p from RangeNdc p where (provider in ?1 )")
	List<RangeNdc>  findRangeNdcbyRule16(List<String> providerList);

	@Query("select p from RangeNdc p where (code in ?1 ) and (provider in ?2 )")
	List<RangeNdc>  findRangeNdcbyRule17(List<String> codeList, List<String> providerList);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and (provider in ?2 )")
	List<RangeNdc>  findRangeNdcbyRule18(List<String> iso2List, List<String> providerList);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and (provider in ?3 )")
	List<RangeNdc>  findRangeNdcbyRule19(List<String> codeList, List<String> iso2List, List<String> providerList);

	@Query("select p from RangeNdc p where (tos in ?1 ) and (provider in ?2 )")
	List<RangeNdc>  findRangeNdcbyRule20(List<String> tosList, List<String> providerList);

	@Query("select p from RangeNdc p where (code in ?1 ) and (tos in ?2 ) and (provider in ?3 )")
	List<RangeNdc>  findRangeNdcbyRule21(List<String> codeList, List<String> tosList, List<String> providerList);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and (tos in ?2 ) and (provider in ?3 )")
	List<RangeNdc>  findRangeNdcbyRule22(List<String> iso2List, List<String> tosList, List<String> providerList);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (provider in ?4 )")
	List<RangeNdc>  findRangeNdcbyRule23(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> providerList);

	@Query("select p from RangeNdc p where (tosdesc in ?1 ) and (provider in ?2 )")
	List<RangeNdc>  findRangeNdcbyRule24(List<String> tosDescList, List<String> providerList);

	@Query("select p from RangeNdc p where (code in ?1 ) and (tosdesc in ?2 ) and (provider in ?3 )")
	List<RangeNdc>  findRangeNdcbyRule25(List<String> codeList, List<String> tosDescList, List<String> providerList);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and (tosdesc in ?2 ) and (provider in ?3 )")
	List<RangeNdc>  findRangeNdcbyRule26(List<String> iso2List, List<String> tosDescList, List<String> providerList);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and (tosdesc in ?3 ) and (provider in ?4 )")
	List<RangeNdc>  findRangeNdcbyRule27(List<String> codeList, List<String> iso2List, List<String> tosDescList, List<String> providerList);

	@Query("select p from RangeNdc p where tos in ?1 or concat(tos,',',tosdesc) in ?2  and (provider in ?3 )")
	List<RangeNdc>  findRangeNdcbyRule28(List<String> tosList, List<String> tosDescList, List<String> providerList);

	@Query("select p from RangeNdc p where (code in ?1 ) and tos in ?2 or concat(tos,',',tosdesc) in ?3  and (provider in ?4 )")
	List<RangeNdc>  findRangeNdcbyRule29(List<String> codeList, List<String> tosList, List<String> tosDescList, List<String> providerList);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and tos in ?2 or concat(tos,',',tosdesc) in ?3  and (provider in ?4 )")
	List<RangeNdc>  findRangeNdcbyRule30(List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and tos in ?3 or concat(tos,',',tosdesc) in ?4  and (provider in ?5 )")
	List<RangeNdc>  findRangeNdcbyRule31(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList);


	
	@Query("select p from RangeNdc p where (code in ?1 )")
	Page<RangeNdc>  findRangeNdcbyRule1(List<String> codeList, Pageable pageable);

	@Query("select p from RangeNdc p where (iso2 in ?1 )")
	Page<RangeNdc>  findRangeNdcbyRule2(List<String> iso2List, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 )")
	Page<RangeNdc>  findRangeNdcbyRule3(List<String> codeList, List<String> iso2List, Pageable pageable);

	@Query("select p from RangeNdc p where (tos in ?1 )")
	Page<RangeNdc>  findRangeNdcbyRule4(List<String> tosList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 ) and (tos in ?2 )")
	Page<RangeNdc>  findRangeNdcbyRule5(List<String> codeList, List<String> tosList, Pageable pageable);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and (tos in ?2 )")
	Page<RangeNdc>  findRangeNdcbyRule6(List<String> iso2List, List<String> tosList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 )")
	Page<RangeNdc>  findRangeNdcbyRule7(List<String> codeList, List<String> iso2List, List<String> tosList, Pageable pageable);

	@Query("select p from RangeNdc p where (tosdesc in ?1 )")
	Page<RangeNdc>  findRangeNdcbyRule8(List<String> tosDescList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 ) and (tosdesc in ?2 )")
	Page<RangeNdc>  findRangeNdcbyRule9(List<String> codeList, List<String> tosDescList, Pageable pageable);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and (tosdesc in ?2 )")
	Page<RangeNdc>  findRangeNdcbyRule10(List<String> iso2List, List<String> tosDescList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and (tosdesc in ?3 )")
	Page<RangeNdc>  findRangeNdcbyRule11(List<String> codeList, List<String> iso2List, List<String> tosDescList, Pageable pageable);

	@Query("select p from RangeNdc p where tos in ?1 or concat(tos,',',tosdesc) in ?2 ")
	Page<RangeNdc>  findRangeNdcbyRule12(List<String> tosList, List<String> tosDescList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 ) and tos in ?2 or concat(tos,',',tosdesc) in ?3 ")
	Page<RangeNdc>  findRangeNdcbyRule13(List<String> codeList, List<String> tosList, List<String> tosDescList, Pageable pageable);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and tos in ?2 or concat(tos,',',tosdesc) in ?3 ")
	Page<RangeNdc>  findRangeNdcbyRule14(List<String> iso2List, List<String> tosList, List<String> tosDescList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and tos in ?3 or concat(tos,',',tosdesc) in ?4 ")
	Page<RangeNdc>  findRangeNdcbyRule15(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, Pageable pageable);

	@Query("select p from RangeNdc p where (provider in ?1 )")
	Page<RangeNdc>  findRangeNdcbyRule16(List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 ) and (provider in ?2 )")
	Page<RangeNdc>  findRangeNdcbyRule17(List<String> codeList, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and (provider in ?2 )")
	Page<RangeNdc>  findRangeNdcbyRule18(List<String> iso2List, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and (provider in ?3 )")
	Page<RangeNdc>  findRangeNdcbyRule19(List<String> codeList, List<String> iso2List, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (tos in ?1 ) and (provider in ?2 )")
	Page<RangeNdc>  findRangeNdcbyRule20(List<String> tosList, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 ) and (tos in ?2 ) and (provider in ?3 )")
	Page<RangeNdc>  findRangeNdcbyRule21(List<String> codeList, List<String> tosList, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and (tos in ?2 ) and (provider in ?3 )")
	Page<RangeNdc>  findRangeNdcbyRule22(List<String> iso2List, List<String> tosList, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (provider in ?4 )")
	Page<RangeNdc>  findRangeNdcbyRule23(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (tosdesc in ?1 ) and (provider in ?2 )")
	Page<RangeNdc>  findRangeNdcbyRule24(List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 ) and (tosdesc in ?2 ) and (provider in ?3 )")
	Page<RangeNdc>  findRangeNdcbyRule25(List<String> codeList, List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and (tosdesc in ?2 ) and (provider in ?3 )")
	Page<RangeNdc>  findRangeNdcbyRule26(List<String> iso2List, List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and (tosdesc in ?3 ) and (provider in ?4 )")
	Page<RangeNdc>  findRangeNdcbyRule27(List<String> codeList, List<String> iso2List, List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where tos in ?1 or concat(tos,',',tosdesc) in ?2  and (provider in ?3 )")
	Page<RangeNdc>  findRangeNdcbyRule28(List<String> tosList, List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 ) and tos in ?2 or concat(tos,',',tosdesc) in ?3  and (provider in ?4 )")
	Page<RangeNdc>  findRangeNdcbyRule29(List<String> codeList, List<String> tosList, List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (iso2 in ?1 ) and tos in ?2 or concat(tos,',',tosdesc) in ?3  and (provider in ?4 )")
	Page<RangeNdc>  findRangeNdcbyRule30(List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from RangeNdc p where (code in ?1 or iso2 in ?2 ) and tos in ?3 or concat(tos,',',tosdesc) in ?4  and (provider in ?5 )")
	Page<RangeNdc>  findRangeNdcbyRule31(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Pageable pageable);


	
}
