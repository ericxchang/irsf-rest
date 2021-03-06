package com.iconectiv.irsf.portal.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface ReadOnlyRepository<T, ID extends Serializable> extends PagingAndSortingRepository <T, ID> {

    T findOne(ID id);

    Page<T> findAll(Pageable pageable);
    
    List<T> findAll();

}
