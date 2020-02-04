package com.github.wladox.moneytransfer.repository;

import com.github.wladox.moneytransfer.model.TransferRecord;

import java.util.Collection;

public interface TransactionRepository {

    void persist(TransferRecord t);

    Collection<TransferRecord> getAll(String accountId);
}
