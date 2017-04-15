package com.iconectiv.irsf.portal.repositories.customer;

import java.util.Collection;

import com.iconectiv.irsf.portal.model.customer.ListDetails;

/**
 * Created by echang on 1/12/2017.
 */
public interface ListDetailsRepositoryCustomer {
    void batchUpdate(Collection<ListDetails> items);
}
