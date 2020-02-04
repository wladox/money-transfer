package com.github.wladox.moneytransfer.repository;

import com.github.wladox.moneytransfer.exceptions.AccountCreationException;
import com.github.wladox.moneytransfer.model.Account;

import java.util.Collection;
import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findById(String id);

    void create(Account newAccount) throws AccountCreationException;

    Collection<Account> findAll();

}
