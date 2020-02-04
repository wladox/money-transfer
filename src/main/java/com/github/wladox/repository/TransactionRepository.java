package com.github.wladox.repository;

import com.github.wladox.model.TransferRecord;

import java.util.Collection;

public interface TransactionRepository {

    void persist(TransferRecord t);

    Collection<TransferRecord> getAll(String accountId);
}
