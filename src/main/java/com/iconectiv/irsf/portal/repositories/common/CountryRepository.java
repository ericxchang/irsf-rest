package com.iconectiv.irsf.portal.repositories.common;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.iconectiv.irsf.portal.model.common.Country;
import com.iconectiv.irsf.portal.repositories.ReadOnlyRepository;

/**
 * Created by echang on 1/12/2017.
 */
public interface CountryRepository extends ReadOnlyRepository <Country, String>{
	@Cacheable("country")
	List<Country> findAll();
}
