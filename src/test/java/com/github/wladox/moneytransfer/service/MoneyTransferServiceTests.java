package com.github.wladox.moneytransfer.service;

import com.github.wladox.moneytransfer.Constants;
import com.github.wladox.moneytransfer.exceptions.AccountNotFoundException;
import com.github.wladox.moneytransfer.exceptions.TransactionException;
import com.github.wladox.moneytransfer.model.Account;
import com.github.wladox.moneytransfer.model.Transfer;
import com.github.wladox.moneytransfer.repository.AccountRepository;
import com.github.wladox.moneytransfer.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;

public class MoneyTransferServiceTests {

    private static final String TEST_SENDER_NUMBER = "test_account_number_1234";
    private static final String TEST_RECIPIENT_NUMBER = "test_account_number_4356";

    MoneyTransferService service;

    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        service = new MoneyTransferService(accountRepository, transactionRepository);
    }

    @Test
    public void givenTransfer_whenInsufficientFunds_thenThrowException() throws AccountNotFoundException, TransactionException {

        Account sender = Account.builder()
                .balance(BigDecimal.ONE)
                .number(TEST_SENDER_NUMBER)
                .id(UUID.randomUUID().toString())
                .build();

        Account recipient = Account.builder()
                .balance(BigDecimal.valueOf(100))
                .number(TEST_RECIPIENT_NUMBER)
                .id(UUID.randomUUID().toString())
                .build();

        doReturn(Optional.of(sender)).when(accountRepository).findById(TEST_SENDER_NUMBER);
        doReturn(Optional.of(recipient)).when(accountRepository).findById(TEST_RECIPIENT_NUMBER);

        Transfer t = Transfer.builder()
                .from(TEST_SENDER_NUMBER)
                .to(TEST_RECIPIENT_NUMBER)
                .amount(BigDecimal.valueOf(100))
                .build();

        thrown.expect(TransactionException.class);
        thrown.expectMessage(Constants.ERROR_INSUFFICIENT_FUNDS);

        service.process(t);
    }


    @Test
    public void givenTransfer_whenNegativeAmount_thenThrowException() throws AccountNotFoundException, TransactionException {

        Account sender = Account.builder()
                .balance(BigDecimal.ONE)
                .number(TEST_SENDER_NUMBER)
                .id(UUID.randomUUID().toString())
                .build();

        Account recipient = Account.builder()
                .balance(BigDecimal.valueOf(100))
                .number(TEST_RECIPIENT_NUMBER)
                .id(UUID.randomUUID().toString())
                .build();

        doReturn(Optional.of(sender)).when(accountRepository).findById(TEST_SENDER_NUMBER);
        doReturn(Optional.of(recipient)).when(accountRepository).findById(TEST_RECIPIENT_NUMBER);

        Transfer t = Transfer.builder()
                .from(TEST_SENDER_NUMBER)
                .to(TEST_RECIPIENT_NUMBER)
                .amount(BigDecimal.valueOf(-100))
                .build();

        thrown.expect(TransactionException.class);
        thrown.expectMessage(Constants.ERROR_NEGATIVE_AMOUNT);

        service.process(t);
    }

    @Test
    public void givenTransfer_whenNonExistingAccount_thenThrowException() throws TransactionException {
        Transfer t = Transfer.builder()
                .from(TEST_SENDER_NUMBER)
                .to(TEST_RECIPIENT_NUMBER)
                .amount(BigDecimal.valueOf(-100))
                .build();

        thrown.expect(TransactionException.class);
        thrown.expectMessage(Constants.ERROR_ACCOUNT_NOT_FOUND);

        service.process(t);
    }

    @Test
    public void givenValidTransfer_whenProcess_thenShouldReturnTransactionId() throws TransactionException {

        Account sender = Account.builder()
                .balance(BigDecimal.valueOf(100))
                .number(TEST_SENDER_NUMBER)
                .id(UUID.randomUUID().toString())
                .build();

        Account recipient = Account.builder()
                .balance(BigDecimal.ZERO)
                .number(TEST_RECIPIENT_NUMBER)
                .id(UUID.randomUUID().toString())
                .build();

        BigDecimal transferAmount = BigDecimal.valueOf(100);

        Transfer t = Transfer.builder()
                .from(TEST_SENDER_NUMBER)
                .to(TEST_RECIPIENT_NUMBER)
                .amount(transferAmount)
                .build();

        doReturn(Optional.of(sender)).when(accountRepository).findById(TEST_SENDER_NUMBER);
        doReturn(Optional.of(recipient)).when(accountRepository).findById(TEST_RECIPIENT_NUMBER);

        Long txId = service.process(t);

        assertThat(txId, is(notNullValue()));
        assertThat(sender.getBalance(), equalTo(BigDecimal.ZERO));
        assertThat(recipient.getBalance(), equalTo(transferAmount));

    }


}
