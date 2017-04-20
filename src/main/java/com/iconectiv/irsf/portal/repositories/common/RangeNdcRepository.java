package com.iconectiv.irsf.portal.repositories.common;

import java.util.List;

import com.iconectiv.irsf.portal.model.common.RangeNdc;
import com.iconectiv.irsf.portal.repositories.ReadOnlyRepository;

/**
 * Created by echang on 1/12/2017.
 */
public interface RangeNdcRepository extends ReadOnlyRepository <RangeNdc, String>{
	//@Cacheable("rangeNDC")
	List<RangeNdc> findAll();
}
