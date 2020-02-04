package com.github.wladox.moneytransfer.service;

import com.github.wladox.moneytransfer.exceptions.AccountNotFoundException;
import com.github.wladox.moneytransfer.exceptions.TransactionException;
import com.github.wladox.moneytransfer.model.Account;
import com.github.wladox.moneytransfer.model.Transfer;
import com.github.wladox.moneytransfer.model.TransferRecord;
import com.github.wladox.moneytransfer.repository.AccountRepository;
import com.github.wladox.moneytransfer.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Random;


public class MoneyTransferService {

    private AccountRepository accountRepository;

    private TransactionRepository transactionRepository;

    public MoneyTransferService(AccountRepository repository, TransactionRepository txRepository) {
        this.accountRepository = repository;
        this.transactionRepository = txRepository;
    }

    public Long process(Transfer tx) throws AccountNotFoundException, TransactionException {
        synchronized (this) {
            Optional<Account> from = accountRepository.findById(tx.getFrom());
            Optional<Account> to = accountRepository.findById(tx.getTo());

            if (from.isEmpty() || to.isEmpty()) {
                throw new AccountNotFoundException("One of the accounts not exists");
            }

            BigDecimal amount  = tx.getAmount();

            validateTransfer(from.get(), amount);

            BigDecimal fromBalance = from.get().getBalance();
            BigDecimal newBalanceFrom = fromBalance.subtract(amount);
            from.get().setBalance(newBalanceFrom);

            BigDecimal toBalance = to.get().getBalance();
            BigDecimal newBalanceTo = toBalance.add(amount);
            to.get().setBalance(newBalanceTo);

            long txId = new Random().nextLong();

            transactionRepository.persist(TransferRecord.builder()
                    .id(txId)
                    .created(new Date(System.currentTimeMillis()))
                    .transfer(tx)
                    .build());

            return txId;
        }

    }

    public Collection<TransferRecord> showHistory(String accountId) {
        return transactionRepository.getAll(accountId);
    }

    private static void validateTransfer(Account account, BigDecimal amount) throws TransactionException {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new TransactionException("Insufficient funds");
        }
    }

}
