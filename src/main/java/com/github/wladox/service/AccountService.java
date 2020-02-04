package com.github.wladox.service;

import com.github.wladox.exceptions.AccountNotFoundException;
import com.github.wladox.model.Account;
import com.github.wladox.repository.AccountRepository;

import java.util.Collection;
import java.util.Optional;

public class AccountService {

    private AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public void create(Account account) {
        repository.create(account);
    }

    public Account getByNumber(String number) throws AccountNotFoundException {
        Optional<Account> acc = repository.findById(number);
        return acc.orElseThrow(() -> new AccountNotFoundException("Account not exists"));
    }

    public Collection<Account> getAll() {
        return repository.findAll();
    }

}
