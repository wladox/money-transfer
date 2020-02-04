package com.github.wladox.repository;

import com.github.wladox.exceptions.AccountCreationException;
import com.github.wladox.model.Account;

import java.util.Collection;
import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findById(String id);

    void create(Account newAccount) throws AccountCreationException;

    Collection<Account> findAll();

}
