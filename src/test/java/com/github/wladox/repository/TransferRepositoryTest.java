package com.github.wladox.repository;

import com.github.wladox.exceptions.AccountNotFoundException;
import com.github.wladox.exceptions.TransactionException;
import com.github.wladox.model.Account;
import com.github.wladox.model.Transfer;
import com.github.wladox.repository.impl.InMemoryAccountRepository;
import com.github.wladox.repository.impl.InMemoryTransactionRepository;
import com.github.wladox.service.MoneyTransferService;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransferRepositoryTest {

    private static final String ACCOUNT_A = "test_account_a";
    private static final String ACCOUNT_B = "test_account_b";

    static final int NUM_OF_THREADS = 100;
    static final int NUM_OF_ITERATIONS = 1000;
    static final BigDecimal TRANSFER_AMOUNT = BigDecimal.valueOf(10);

    AccountRepository accountRepository;

    MoneyTransferService moneyTransferService;


    @Before
    public void setUp() {

        accountRepository = InMemoryAccountRepository.getInstance();
        accountRepository.create(new Account()
                .setNumber(ACCOUNT_A)
                .setBalance(BigDecimal.valueOf(10000000)));
        accountRepository.create(new Account()
                .setNumber(ACCOUNT_B)
                .setBalance(BigDecimal.valueOf(10000000)));

        moneyTransferService = new MoneyTransferService(
                accountRepository,
                InMemoryTransactionRepository.getInstance());
    }

    @Test
    public void testConcurrentTranfers() throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(NUM_OF_THREADS);

        Account a = accountRepository.findById(ACCOUNT_A).get();
        Account b = accountRepository.findById(ACCOUNT_B).get();

        BigDecimal balanceA = a.getBalance();
        BigDecimal balanceB = b.getBalance();

        BigDecimal totalBalanceChange = TRANSFER_AMOUNT
                .multiply(BigDecimal.valueOf(NUM_OF_THREADS))
                .multiply(BigDecimal.valueOf(NUM_OF_ITERATIONS));

        BigDecimal targetBalanceA = balanceA.subtract(totalBalanceChange);
        BigDecimal targetBalanceB = balanceB.add(totalBalanceChange);

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            executor.submit(() -> {

                for (int i1 = 0; i1 < NUM_OF_ITERATIONS; i1++) {

                    Transfer t = Transfer.builder()
                            .from(ACCOUNT_A)
                            .to(ACCOUNT_B)
                            .amount(TRANSFER_AMOUNT)
                            .build();

                    try {
                        moneyTransferService.process(t);
                    } catch (TransactionException | AccountNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        Assert.assertThat(a.getBalance(), CoreMatchers.equalTo(targetBalanceA));
        Assert.assertThat(b.getBalance(), CoreMatchers.equalTo(targetBalanceB));
    }
}
