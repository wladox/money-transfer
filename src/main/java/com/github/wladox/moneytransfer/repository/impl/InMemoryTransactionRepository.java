package com.github.wladox.moneytransfer.repository.impl;

import com.github.wladox.moneytransfer.model.TransferRecord;
import com.github.wladox.moneytransfer.repository.TransactionRepository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTransactionRepository implements TransactionRepository {

    private static final Map<String, LinkedList<TransferRecord>> LOG = new ConcurrentHashMap<>();

    private static final InMemoryTransactionRepository instance = new InMemoryTransactionRepository();

    private InMemoryTransactionRepository() { }

    public static InMemoryTransactionRepository getInstance() {
        return instance;
    }

    @Override
    synchronized public void persist(TransferRecord t) {
        String from = t.getTransfer().getFrom();
        String to = t.getTransfer().getTo();

        LinkedList<TransferRecord> fromHistory = LOG.getOrDefault(from, new LinkedList<>());
        fromHistory.addFirst(t);
        
        LinkedList<TransferRecord> toHistory = LOG.getOrDefault(to, new LinkedList<>());
        toHistory.addFirst(t);

        LOG.put(from, fromHistory);

        LOG.put(to, toHistory);
    }

    @Override
    public Collection<TransferRecord> getAll(String accountId) {
        return LOG.getOrDefault(accountId, new LinkedList<>());
    }

}
