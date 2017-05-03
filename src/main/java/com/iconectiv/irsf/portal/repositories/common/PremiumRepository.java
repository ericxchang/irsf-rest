package com.iconectiv.irsf.portal.repositories.common;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.iconectiv.irsf.portal.model.common.Premium;
import com.iconectiv.irsf.portal.repositories.ReadOnlyRepository;


public interface PremiumRepository extends ReadOnlyRepository<Premium, Integer>{

	
	@Query("select p from Premium p where (code in ?1 )")
	Page<Premium>  findPremiumRangeByRule1(List<String> codeList, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 )")
	Page<Premium>  findPremiumRangeByRule2(List<String> iso2List, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 )")
	Page<Premium>  findPremiumRangeByRule3(List<String> codeList, List<String> iso2List, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 )")
	Page<Premium>  findPremiumRangeByRule4(List<String> tosList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 )")
	Page<Premium>  findPremiumRangeByRule5(List<String> codeList, List<String> tosList, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 )")
	Page<Premium>  findPremiumRangeByRule6(List<String> iso2List, List<String> tosList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 )")
	Page<Premium>  findPremiumRangeByRule7(List<String> codeList, List<String> iso2List, List<String> tosList, Pageable pageable);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 )")
	Page<Premium>  findPremiumRangeByRule8(List<String> tosDescList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 )")
	Page<Premium>  findPremiumRangeByRule9(List<String> codeList, List<String> tosDescList, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 )")
	Page<Premium>  findPremiumRangeByRule10(List<String> iso2List, List<String> tosDescList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 )")
	Page<Premium>  findPremiumRangeByRule11(List<String> codeList, List<String> iso2List, List<String> tosDescList, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2) ")
	Page<Premium>  findPremiumRangeByRule12(List<String> tosList, List<String> tosDescList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3) ")
	Page<Premium>  findPremiumRangeByRule13(List<String> codeList, List<String> tosList, List<String> tosDescList, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3) ")
	Page<Premium>  findPremiumRangeByRule14(List<String> iso2List, List<String> tosList, List<String> tosDescList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4) ")
	Page<Premium>  findPremiumRangeByRule15(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, Pageable pageable);

	@Query("select p from Premium p where (provider in ?1 )")
	Page<Premium>  findPremiumRangeByRule16(List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (provider in ?2 )")
	Page<Premium>  findPremiumRangeByRule17(List<String> codeList, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (provider in ?2 )")
	Page<Premium>  findPremiumRangeByRule18(List<String> iso2List, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (provider in ?3 )")
	Page<Premium>  findPremiumRangeByRule19(List<String> codeList, List<String> iso2List, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 ) and (provider in ?2 )")
	Page<Premium>  findPremiumRangeByRule20(List<String> tosList, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (provider in ?3 )")
	Page<Premium>  findPremiumRangeByRule21(List<String> codeList, List<String> tosList, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (provider in ?3 )")
	Page<Premium>  findPremiumRangeByRule22(List<String> iso2List, List<String> tosList, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (provider in ?4 )")
	Page<Premium>  findPremiumRangeByRule23(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (provider in ?2 )")
	Page<Premium>  findPremiumRangeByRule24(List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 )")
	Page<Premium>  findPremiumRangeByRule25(List<String> codeList, List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 )")
	Page<Premium>  findPremiumRangeByRule26(List<String> iso2List, List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (provider in ?4 )")
	Page<Premium>  findPremiumRangeByRule27(List<String> codeList, List<String> iso2List, List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (provider in ?3 )")
	Page<Premium>  findPremiumRangeByRule28(List<String> tosList, List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 )")
	Page<Premium>  findPremiumRangeByRule29(List<String> codeList, List<String> tosList, List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 )")
	Page<Premium>  findPremiumRangeByRule30(List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (provider in ?5 )")
	Page<Premium>  findPremiumRangeByRule31(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Pageable pageable);

	@Query("select p from Premium p where (lastUpdate >= ?1 )")
	Page<Premium>  findPremiumRangeByRule32(Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (lastUpdate >= ?2 )")
	Page<Premium>  findPremiumRangeByRule33(List<String> codeList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (lastUpdate >= ?2 )")
	Page<Premium>  findPremiumRangeByRule34(List<String> iso2List, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (lastUpdate >= ?3 )")
	Page<Premium>  findPremiumRangeByRule35(List<String> codeList, List<String> iso2List, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 ) and (lastUpdate >= ?2 )")
	Page<Premium>  findPremiumRangeByRule36(List<String> tosList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (lastUpdate >= ?3 )")
	Page<Premium>  findPremiumRangeByRule37(List<String> codeList, List<String> tosList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (lastUpdate >= ?3 )")
	Page<Premium>  findPremiumRangeByRule38(List<String> iso2List, List<String> tosList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (lastUpdate >= ?4 )")
	Page<Premium>  findPremiumRangeByRule39(List<String> codeList, List<String> iso2List, List<String> tosList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (lastUpdate >= ?2 )")
	Page<Premium>  findPremiumRangeByRule40(List<String> tosDescList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (lastUpdate >= ?3 )")
	Page<Premium>  findPremiumRangeByRule41(List<String> codeList, List<String> tosDescList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (lastUpdate >= ?3 )")
	Page<Premium>  findPremiumRangeByRule42(List<String> iso2List, List<String> tosDescList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (lastUpdate >= ?4 )")
	Page<Premium>  findPremiumRangeByRule43(List<String> codeList, List<String> iso2List, List<String> tosDescList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (lastUpdate >= ?3 )")
	Page<Premium>  findPremiumRangeByRule44(List<String> tosList, List<String> tosDescList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (lastUpdate >= ?4 )")
	Page<Premium>  findPremiumRangeByRule45(List<String> codeList, List<String> tosList, List<String> tosDescList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (lastUpdate >= ?4 )")
	Page<Premium>  findPremiumRangeByRule46(List<String> iso2List, List<String> tosList, List<String> tosDescList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (lastUpdate >= ?5 )")
	Page<Premium>  findPremiumRangeByRule47(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (provider in ?1 ) and (lastUpdate >= ?2 )")
	Page<Premium>  findPremiumRangeByRule48(List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 )")
	Page<Premium>  findPremiumRangeByRule49(List<String> codeList, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 )")
	Page<Premium>  findPremiumRangeByRule50(List<String> iso2List, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 )")
	Page<Premium>  findPremiumRangeByRule51(List<String> codeList, List<String> iso2List, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 )")
	Page<Premium>  findPremiumRangeByRule52(List<String> tosList, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 )")
	Page<Premium>  findPremiumRangeByRule53(List<String> codeList, List<String> tosList, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 )")
	Page<Premium>  findPremiumRangeByRule54(List<String> iso2List, List<String> tosList, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (provider in ?4 ) and (lastUpdate >= ?5 )")
	Page<Premium>  findPremiumRangeByRule55(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 )")
	Page<Premium>  findPremiumRangeByRule56(List<String> tosDescList, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 )")
	Page<Premium>  findPremiumRangeByRule57(List<String> codeList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 )")
	Page<Premium>  findPremiumRangeByRule58(List<String> iso2List, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (provider in ?4 ) and (lastUpdate >= ?5 )")
	Page<Premium>  findPremiumRangeByRule59(List<String> codeList, List<String> iso2List, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (provider in ?3 ) and (lastUpdate >= ?4 )")
	Page<Premium>  findPremiumRangeByRule60(List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 ) and (lastUpdate >= ?5 )")
	Page<Premium>  findPremiumRangeByRule61(List<String> codeList, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 ) and (lastUpdate >= ?5 )")
	Page<Premium>  findPremiumRangeByRule62(List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (provider in ?5 ) and (lastUpdate >= ?6 )")
	Page<Premium>  findPremiumRangeByRule63(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Pageable pageable);

	@Query("select p from Premium p where (lastUpdate <= ?1 )")
	Page<Premium>  findPremiumRangeByRule64(Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (lastUpdate <= ?2 )")
	Page<Premium>  findPremiumRangeByRule65(List<String> codeList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (lastUpdate <= ?2 )")
	Page<Premium>  findPremiumRangeByRule66(List<String> iso2List, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule67(List<String> codeList, List<String> iso2List, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 ) and (lastUpdate <= ?2 )")
	Page<Premium>  findPremiumRangeByRule68(List<String> tosList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule69(List<String> codeList, List<String> tosList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule70(List<String> iso2List, List<String> tosList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule71(List<String> codeList, List<String> iso2List, List<String> tosList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (lastUpdate <= ?2 )")
	Page<Premium>  findPremiumRangeByRule72(List<String> tosDescList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule73(List<String> codeList, List<String> tosDescList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule74(List<String> iso2List, List<String> tosDescList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule75(List<String> codeList, List<String> iso2List, List<String> tosDescList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule76(List<String> tosList, List<String> tosDescList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule77(List<String> codeList, List<String> tosList, List<String> tosDescList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule78(List<String> iso2List, List<String> tosList, List<String> tosDescList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule79(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (provider in ?1 ) and (lastUpdate <= ?2 )")
	Page<Premium>  findPremiumRangeByRule80(List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (provider in ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule81(List<String> codeList, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (provider in ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule82(List<String> iso2List, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (provider in ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule83(List<String> codeList, List<String> iso2List, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 ) and (provider in ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule84(List<String> tosList, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (provider in ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule85(List<String> codeList, List<String> tosList, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (provider in ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule86(List<String> iso2List, List<String> tosList, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (provider in ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule87(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (provider in ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule88(List<String> tosDescList, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule89(List<String> codeList, List<String> tosDescList, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule90(List<String> iso2List, List<String> tosDescList, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (provider in ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule91(List<String> codeList, List<String> iso2List, List<String> tosDescList, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (provider in ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule92(List<String> tosList, List<String> tosDescList, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule93(List<String> codeList, List<String> tosList, List<String> tosDescList, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule94(List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (provider in ?5 ) and (lastUpdate <= ?6 )")
	Page<Premium>  findPremiumRangeByRule95(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (lastUpdate >= ?1 ) and (lastUpdate <= ?2 )")
	Page<Premium>  findPremiumRangeByRule96(Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (lastUpdate >= ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule97(List<String> codeList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (lastUpdate >= ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule98(List<String> iso2List, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule99(List<String> codeList, List<String> iso2List, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 ) and (lastUpdate >= ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule100(List<String> tosList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule101(List<String> codeList, List<String> tosList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule102(List<String> iso2List, List<String> tosList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule103(List<String> codeList, List<String> iso2List, List<String> tosList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (lastUpdate >= ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule104(List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule105(List<String> codeList, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule106(List<String> iso2List, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule107(List<String> codeList, List<String> iso2List, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule108(List<String> tosList, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule109(List<String> codeList, List<String> tosList, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule110(List<String> iso2List, List<String> tosList, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (lastUpdate >= ?5 ) and (lastUpdate <= ?6 )")
	Page<Premium>  findPremiumRangeByRule111(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (provider in ?1 ) and (lastUpdate >= ?2 ) and (lastUpdate <= ?3 )")
	Page<Premium>  findPremiumRangeByRule112(List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule113(List<String> codeList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule114(List<String> iso2List, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule115(List<String> codeList, List<String> iso2List, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule116(List<String> tosList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule117(List<String> codeList, List<String> tosList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule118(List<String> iso2List, List<String> tosList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (provider in ?4 ) and (lastUpdate >= ?5 ) and (lastUpdate <= ?6 )")
	Page<Premium>  findPremiumRangeByRule119(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	Page<Premium>  findPremiumRangeByRule120(List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule121(List<String> codeList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule122(List<String> iso2List, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (provider in ?4 ) and (lastUpdate >= ?5 ) and (lastUpdate <= ?6 )")
	Page<Premium>  findPremiumRangeByRule123(List<String> codeList, List<String> iso2List, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (provider in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	Page<Premium>  findPremiumRangeByRule124(List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 ) and (lastUpdate >= ?5 ) and (lastUpdate <= ?6 )")
	Page<Premium>  findPremiumRangeByRule125(List<String> codeList, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 ) and (lastUpdate >= ?5 ) and (lastUpdate <= ?6 )")
	Page<Premium>  findPremiumRangeByRule126(List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (provider in ?5 ) and (lastUpdate >= ?6 ) and (lastUpdate <= ?7 )")
	Page<Premium>  findPremiumRangeByRule127(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved, Pageable pageable);


	
	
	
	@Query("select p from Premium p where (code in ?1 )")
	List<Premium>  findPremiumRangeByRule1(List<String> codeList);

	@Query("select p from Premium p where (iso2 in ?1 )")
	List<Premium>  findPremiumRangeByRule2(List<String> iso2List);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 )")
	List<Premium>  findPremiumRangeByRule3(List<String> codeList, List<String> iso2List);

	@Query("select p from Premium p where (tos in ?1 )")
	List<Premium>  findPremiumRangeByRule4(List<String> tosList);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 )")
	List<Premium>  findPremiumRangeByRule5(List<String> codeList, List<String> tosList);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 )")
	List<Premium>  findPremiumRangeByRule6(List<String> iso2List, List<String> tosList);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 )")
	List<Premium>  findPremiumRangeByRule7(List<String> codeList, List<String> iso2List, List<String> tosList);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 )")
	List<Premium>  findPremiumRangeByRule8(List<String> tosDescList);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 )")
	List<Premium>  findPremiumRangeByRule9(List<String> codeList, List<String> tosDescList);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 )")
	List<Premium>  findPremiumRangeByRule10(List<String> iso2List, List<String> tosDescList);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 )")
	List<Premium>  findPremiumRangeByRule11(List<String> codeList, List<String> iso2List, List<String> tosDescList);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2) ")
	List<Premium>  findPremiumRangeByRule12(List<String> tosList, List<String> tosDescList);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3) ")
	List<Premium>  findPremiumRangeByRule13(List<String> codeList, List<String> tosList, List<String> tosDescList);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3) ")
	List<Premium>  findPremiumRangeByRule14(List<String> iso2List, List<String> tosList, List<String> tosDescList);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4) ")
	List<Premium>  findPremiumRangeByRule15(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList);

	@Query("select p from Premium p where (provider in ?1 )")
	List<Premium>  findPremiumRangeByRule16(List<String> providerList);

	@Query("select p from Premium p where (code in ?1 ) and (provider in ?2 )")
	List<Premium>  findPremiumRangeByRule17(List<String> codeList, List<String> providerList);

	@Query("select p from Premium p where (iso2 in ?1 ) and (provider in ?2 )")
	List<Premium>  findPremiumRangeByRule18(List<String> iso2List, List<String> providerList);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (provider in ?3 )")
	List<Premium>  findPremiumRangeByRule19(List<String> codeList, List<String> iso2List, List<String> providerList);

	@Query("select p from Premium p where (tos in ?1 ) and (provider in ?2 )")
	List<Premium>  findPremiumRangeByRule20(List<String> tosList, List<String> providerList);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (provider in ?3 )")
	List<Premium>  findPremiumRangeByRule21(List<String> codeList, List<String> tosList, List<String> providerList);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (provider in ?3 )")
	List<Premium>  findPremiumRangeByRule22(List<String> iso2List, List<String> tosList, List<String> providerList);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (provider in ?4 )")
	List<Premium>  findPremiumRangeByRule23(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> providerList);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (provider in ?2 )")
	List<Premium>  findPremiumRangeByRule24(List<String> tosDescList, List<String> providerList);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 )")
	List<Premium>  findPremiumRangeByRule25(List<String> codeList, List<String> tosDescList, List<String> providerList);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 )")
	List<Premium>  findPremiumRangeByRule26(List<String> iso2List, List<String> tosDescList, List<String> providerList);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (provider in ?4 )")
	List<Premium>  findPremiumRangeByRule27(List<String> codeList, List<String> iso2List, List<String> tosDescList, List<String> providerList);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (provider in ?3 )")
	List<Premium>  findPremiumRangeByRule28(List<String> tosList, List<String> tosDescList, List<String> providerList);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 )")
	List<Premium>  findPremiumRangeByRule29(List<String> codeList, List<String> tosList, List<String> tosDescList, List<String> providerList);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 )")
	List<Premium>  findPremiumRangeByRule30(List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (provider in ?5 )")
	List<Premium>  findPremiumRangeByRule31(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList);

	@Query("select p from Premium p where (lastUpdate >= ?1 )")
	List<Premium>  findPremiumRangeByRule32(Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (lastUpdate >= ?2 )")
	List<Premium>  findPremiumRangeByRule33(List<String> codeList, Date afterLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (lastUpdate >= ?2 )")
	List<Premium>  findPremiumRangeByRule34(List<String> iso2List, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (lastUpdate >= ?3 )")
	List<Premium>  findPremiumRangeByRule35(List<String> codeList, List<String> iso2List, Date afterLastObserved);

	@Query("select p from Premium p where (tos in ?1 ) and (lastUpdate >= ?2 )")
	List<Premium>  findPremiumRangeByRule36(List<String> tosList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (lastUpdate >= ?3 )")
	List<Premium>  findPremiumRangeByRule37(List<String> codeList, List<String> tosList, Date afterLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (lastUpdate >= ?3 )")
	List<Premium>  findPremiumRangeByRule38(List<String> iso2List, List<String> tosList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (lastUpdate >= ?4 )")
	List<Premium>  findPremiumRangeByRule39(List<String> codeList, List<String> iso2List, List<String> tosList, Date afterLastObserved);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (lastUpdate >= ?2 )")
	List<Premium>  findPremiumRangeByRule40(List<String> tosDescList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (lastUpdate >= ?3 )")
	List<Premium>  findPremiumRangeByRule41(List<String> codeList, List<String> tosDescList, Date afterLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (lastUpdate >= ?3 )")
	List<Premium>  findPremiumRangeByRule42(List<String> iso2List, List<String> tosDescList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (lastUpdate >= ?4 )")
	List<Premium>  findPremiumRangeByRule43(List<String> codeList, List<String> iso2List, List<String> tosDescList, Date afterLastObserved);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (lastUpdate >= ?3 )")
	List<Premium>  findPremiumRangeByRule44(List<String> tosList, List<String> tosDescList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (lastUpdate >= ?4 )")
	List<Premium>  findPremiumRangeByRule45(List<String> codeList, List<String> tosList, List<String> tosDescList, Date afterLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (lastUpdate >= ?4 )")
	List<Premium>  findPremiumRangeByRule46(List<String> iso2List, List<String> tosList, List<String> tosDescList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (lastUpdate >= ?5 )")
	List<Premium>  findPremiumRangeByRule47(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, Date afterLastObserved);

	@Query("select p from Premium p where (provider in ?1 ) and (lastUpdate >= ?2 )")
	List<Premium>  findPremiumRangeByRule48(List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 )")
	List<Premium>  findPremiumRangeByRule49(List<String> codeList, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 )")
	List<Premium>  findPremiumRangeByRule50(List<String> iso2List, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 )")
	List<Premium>  findPremiumRangeByRule51(List<String> codeList, List<String> iso2List, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (tos in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 )")
	List<Premium>  findPremiumRangeByRule52(List<String> tosList, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 )")
	List<Premium>  findPremiumRangeByRule53(List<String> codeList, List<String> tosList, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 )")
	List<Premium>  findPremiumRangeByRule54(List<String> iso2List, List<String> tosList, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (provider in ?4 ) and (lastUpdate >= ?5 )")
	List<Premium>  findPremiumRangeByRule55(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 )")
	List<Premium>  findPremiumRangeByRule56(List<String> tosDescList, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 )")
	List<Premium>  findPremiumRangeByRule57(List<String> codeList, List<String> tosDescList, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 )")
	List<Premium>  findPremiumRangeByRule58(List<String> iso2List, List<String> tosDescList, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (provider in ?4 ) and (lastUpdate >= ?5 )")
	List<Premium>  findPremiumRangeByRule59(List<String> codeList, List<String> iso2List, List<String> tosDescList, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (provider in ?3 ) and (lastUpdate >= ?4 )")
	List<Premium>  findPremiumRangeByRule60(List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 ) and (lastUpdate >= ?5 )")
	List<Premium>  findPremiumRangeByRule61(List<String> codeList, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 ) and (lastUpdate >= ?5 )")
	List<Premium>  findPremiumRangeByRule62(List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (provider in ?5 ) and (lastUpdate >= ?6 )")
	List<Premium>  findPremiumRangeByRule63(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved);

	@Query("select p from Premium p where (lastUpdate <= ?1 )")
	List<Premium>  findPremiumRangeByRule64(Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (lastUpdate <= ?2 )")
	List<Premium>  findPremiumRangeByRule65(List<String> codeList, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (lastUpdate <= ?2 )")
	List<Premium>  findPremiumRangeByRule66(List<String> iso2List, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule67(List<String> codeList, List<String> iso2List, Date beforeLastObserved);

	@Query("select p from Premium p where (tos in ?1 ) and (lastUpdate <= ?2 )")
	List<Premium>  findPremiumRangeByRule68(List<String> tosList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule69(List<String> codeList, List<String> tosList, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule70(List<String> iso2List, List<String> tosList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule71(List<String> codeList, List<String> iso2List, List<String> tosList, Date beforeLastObserved);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (lastUpdate <= ?2 )")
	List<Premium>  findPremiumRangeByRule72(List<String> tosDescList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule73(List<String> codeList, List<String> tosDescList, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule74(List<String> iso2List, List<String> tosDescList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule75(List<String> codeList, List<String> iso2List, List<String> tosDescList, Date beforeLastObserved);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule76(List<String> tosList, List<String> tosDescList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule77(List<String> codeList, List<String> tosList, List<String> tosDescList, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule78(List<String> iso2List, List<String> tosList, List<String> tosDescList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule79(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, Date beforeLastObserved);

	@Query("select p from Premium p where (provider in ?1 ) and (lastUpdate <= ?2 )")
	List<Premium>  findPremiumRangeByRule80(List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (provider in ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule81(List<String> codeList, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (provider in ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule82(List<String> iso2List, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (provider in ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule83(List<String> codeList, List<String> iso2List, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (tos in ?1 ) and (provider in ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule84(List<String> tosList, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (provider in ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule85(List<String> codeList, List<String> tosList, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (provider in ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule86(List<String> iso2List, List<String> tosList, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (provider in ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule87(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (provider in ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule88(List<String> tosDescList, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule89(List<String> codeList, List<String> tosDescList, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule90(List<String> iso2List, List<String> tosDescList, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (provider in ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule91(List<String> codeList, List<String> iso2List, List<String> tosDescList, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (provider in ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule92(List<String> tosList, List<String> tosDescList, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule93(List<String> codeList, List<String> tosList, List<String> tosDescList, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule94(List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (provider in ?5 ) and (lastUpdate <= ?6 )")
	List<Premium>  findPremiumRangeByRule95(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date beforeLastObserved);

	@Query("select p from Premium p where (lastUpdate >= ?1 ) and (lastUpdate <= ?2 )")
	List<Premium>  findPremiumRangeByRule96(Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (lastUpdate >= ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule97(List<String> codeList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (lastUpdate >= ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule98(List<String> iso2List, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule99(List<String> codeList, List<String> iso2List, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (tos in ?1 ) and (lastUpdate >= ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule100(List<String> tosList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule101(List<String> codeList, List<String> tosList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule102(List<String> iso2List, List<String> tosList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule103(List<String> codeList, List<String> iso2List, List<String> tosList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (lastUpdate >= ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule104(List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule105(List<String> codeList, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule106(List<String> iso2List, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule107(List<String> codeList, List<String> iso2List, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule108(List<String> tosList, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule109(List<String> codeList, List<String> tosList, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule110(List<String> iso2List, List<String> tosList, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (lastUpdate >= ?5 ) and (lastUpdate <= ?6 )")
	List<Premium>  findPremiumRangeByRule111(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (provider in ?1 ) and (lastUpdate >= ?2 ) and (lastUpdate <= ?3 )")
	List<Premium>  findPremiumRangeByRule112(List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule113(List<String> codeList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule114(List<String> iso2List, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule115(List<String> codeList, List<String> iso2List, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (tos in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule116(List<String> tosList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule117(List<String> codeList, List<String> tosList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule118(List<String> iso2List, List<String> tosList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 ) and (provider in ?4 ) and (lastUpdate >= ?5 ) and (lastUpdate <= ?6 )")
	List<Premium>  findPremiumRangeByRule119(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (concat(tos,',',tosdesc) in ?1 ) and (provider in ?2 ) and (lastUpdate >= ?3 ) and (lastUpdate <= ?4 )")
	List<Premium>  findPremiumRangeByRule120(List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule121(List<String> codeList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (concat(tos,',',tosdesc) in ?2 ) and (provider in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule122(List<String> iso2List, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (concat(tos,',',tosdesc) in ?3 ) and (provider in ?4 ) and (lastUpdate >= ?5 ) and (lastUpdate <= ?6 )")
	List<Premium>  findPremiumRangeByRule123(List<String> codeList, List<String> iso2List, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (tos in ?1 or concat(tos,',',tosdesc) in ?2)  and (provider in ?3 ) and (lastUpdate >= ?4 ) and (lastUpdate <= ?5 )")
	List<Premium>  findPremiumRangeByRule124(List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 ) and (lastUpdate >= ?5 ) and (lastUpdate <= ?6 )")
	List<Premium>  findPremiumRangeByRule125(List<String> codeList, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (iso2 in ?1 ) and (tos in ?2 or concat(tos,',',tosdesc) in ?3)  and (provider in ?4 ) and (lastUpdate >= ?5 ) and (lastUpdate <= ?6 )")
	List<Premium>  findPremiumRangeByRule126(List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);

	@Query("select p from Premium p where (code in ?1 or iso2 in ?2 ) and (tos in ?3 or concat(tos,',',tosdesc) in ?4)  and (provider in ?5 ) and (lastUpdate >= ?6 ) and (lastUpdate <= ?7 )")
	List<Premium>  findPremiumRangeByRule127(List<String> codeList, List<String> iso2List, List<String> tosList, List<String> tosDescList, List<String> providerList, Date afterLastObserved, Date beforeLastObserved);



	
}
