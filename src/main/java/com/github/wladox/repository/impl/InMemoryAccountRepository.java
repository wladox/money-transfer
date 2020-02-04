package com.github.wladox.repository.impl;

import com.github.wladox.exceptions.AccountCreationException;
import com.github.wladox.model.Account;
import com.github.wladox.repository.AccountRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryAccountRepository implements AccountRepository {

    private final Map<String, Account> STORE = new ConcurrentHashMap<>();

    private static InMemoryAccountRepository instance = new InMemoryAccountRepository();

    private InMemoryAccountRepository() { }

    public static InMemoryAccountRepository getInstance() {
        return instance;
    }

    @Override
    public Optional<Account> findById(String id) {
        return Optional.ofNullable(STORE.getOrDefault(id, null));
    }

    @Override
    public void create(Account newAccount) throws AccountCreationException{
        checkDuplicate(newAccount);
        newAccount.setId(UUID.randomUUID().toString());
        STORE.put(newAccount.getNumber(), newAccount);
    }

    @Override
    public Collection<Account> findAll() {
        return STORE.values();
    }

    private synchronized void checkDuplicate(Account account) throws AccountCreationException {
        for (String key : STORE.keySet()) {
            if (key.equals(account.getNumber())) {
                throw new AccountCreationException("Duplicate found");
            }
        }
    }
}
