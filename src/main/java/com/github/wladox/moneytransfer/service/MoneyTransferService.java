package com.github.wladox.moneytransfer.service;

import com.github.wladox.moneytransfer.Constants;
import com.github.wladox.moneytransfer.exceptions.AccountNotFoundException;
import com.github.wladox.moneytransfer.exceptions.TransactionException;
import com.github.wladox.moneytransfer.model.Account;
import com.github.wladox.moneytransfer.model.Transfer;
import com.github.wladox.moneytransfer.model.TransferRecord;
import com.github.wladox.moneytransfer.repository.AccountRepository;
import com.github.wladox.moneytransfer.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.*;


public class MoneyTransferService {

    private AccountRepository accountRepository;

    private TransactionRepository transactionRepository;

    public MoneyTransferService(AccountRepository repository, TransactionRepository txRepository) {
        this.accountRepository = repository;
        this.transactionRepository = txRepository;
    }

    public String process(Transfer tx) throws TransactionException {
        synchronized (this) {

            Optional<Account> from  = accountRepository.findById(tx.getFrom());
            Optional<Account> to    = accountRepository.findById(tx.getTo());

            if (from.isEmpty() || to.isEmpty()) {
                throw new TransactionException(Constants.ERROR_ACCOUNT_NOT_FOUND);
            }

            BigDecimal amount  = tx.getAmount();

            validateTransfer(from.get(), amount);

            BigDecimal fromBalance = from.get().getBalance();
            BigDecimal newBalanceFrom = fromBalance.subtract(amount);
            from.get().setBalance(newBalanceFrom);

            BigDecimal toBalance = to.get().getBalance();
            BigDecimal newBalanceTo = toBalance.add(amount);
            to.get().setBalance(newBalanceTo);

            String txId = UUID.randomUUID().toString();

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
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new TransactionException(Constants.ERROR_NEGATIVE_AMOUNT);
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new TransactionException(Constants.ERROR_INSUFFICIENT_FUNDS);
        }
    }

}
