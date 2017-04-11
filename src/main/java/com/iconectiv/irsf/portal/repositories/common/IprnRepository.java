package com.iconectiv.irsf.portal.repositories.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.iconectiv.irsf.portal.model.common.Iprn;
import com.iconectiv.irsf.portal.repositories.ReadOnlyRepository;

/**
 * Created by echang on 1/12/2017.
 */
public interface IprnRepository extends ReadOnlyRepository <Iprn, Integer>{
	Page<Iprn> findAll(Pageable pageable);
}
