package com.github.wladox.repository;

import com.github.wladox.repository.impl.InMemoryAccountRepository;
import org.junit.Before;
import org.junit.Test;

public class AccountRepositoryTests {

    AccountRepository accountRepository;

    @Before
    public void setUp() {
        accountRepository = InMemoryAccountRepository.getInstance();
    }


    @Test
    public void given() {

    }
}
