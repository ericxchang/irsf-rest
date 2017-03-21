package com.iconectiv.irsf.portal.repositories.customer;

import java.util.List;

import com.iconectiv.irsf.portal.model.customer.ListDetails;

/**
 * Created by echang on 1/12/2017.
 */
public interface ListDetailsRepositoryCustomer {
    List<ListDetails> queryJoinByListRefId(int listId);
    List<ListDetails> queryJoinByListRefId(int listId, int startId, int size);
}
