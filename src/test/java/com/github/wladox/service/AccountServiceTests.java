package com.github.wladox.service;

import com.github.wladox.exceptions.AccountCreationException;
import com.github.wladox.exceptions.AccountNotFoundException;
import com.github.wladox.exceptions.TransactionException;
import com.github.wladox.model.Account;
import com.github.wladox.repository.AccountRepository;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;


public class AccountServiceTests {

    private static final String TEST_ACCOUNT_NUMBER = "testAccount1234";

    private AccountService accountService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        AccountRepository repository = Mockito.mock(AccountRepository.class);

        Mockito.when(repository.findById(anyString())).thenReturn(Optional.empty());

        accountService = new AccountService(repository);
    }

    @Test
    public void givenDuplicateAccount_whenCreate_thenShouldThrowException() {

        Account account = new Account().setNumber(TEST_ACCOUNT_NUMBER);

        accountService.create(account);

        accountService.create(account);

        thrown.expect(AccountCreationException.class);
    }

    @Test
    public void givenAccount_whenWithdraw_thenShouldReturnProperValue() throws TransactionException {
        Account account = new Account()
                .setId(UUID.randomUUID().toString())
                .setNumber("testAccount1234");

//        account.getBalance().(BigDecimal.valueOf(50));
        Assert.assertThat(account.getBalance(), CoreMatchers.is(BigDecimal.valueOf(50)));
    }

    @Test(expected = AccountNotFoundException.class)
    public void givenNonExistingAccount_whenFind_thenShouldThrowException() throws AccountNotFoundException {
        accountService.getByNumber(TEST_ACCOUNT_NUMBER);
    }


}
