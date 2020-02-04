package com.github.wladox.moneytransfer.controller;

import com.github.wladox.moneytransfer.Constants;
import com.github.wladox.moneytransfer.exceptions.AccountNotFoundException;
import com.github.wladox.moneytransfer.exceptions.ApiException;
import com.github.wladox.moneytransfer.model.Account;
import com.github.wladox.moneytransfer.service.AccountService;
import com.github.wladox.moneytransfer.util.Helper;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class AccountsController {

    private static final Logger log = LoggerFactory.getLogger(AccountsController.class);

    private AccountService accountService;

    public AccountsController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Creates a new user account
     *
     * @param exchange Client connection object
     */
    public void create(HttpServerExchange exchange) throws ApiException{
        Account account = Helper.getFrom(exchange, Account.class);
        validate(account);
        accountService.create(account);
        exchange.setStatusCode(201);
    }

    /**
     * Retrieves a single user account
     *
     * @param exchange Client connection object
     */
    public void get(HttpServerExchange exchange) throws ApiException {
        String id = Helper.getAccountId(exchange);
        if (id == null) {
            throw new ApiException(400, Constants.ERROR_MISSING_PATH_PARAM);
        }
        Account account;
        try {
            account = accountService.getByNumber(id);
        } catch (AccountNotFoundException e) {
            log.error("Account not found.  Id: {}", id);
            throw new ApiException(404, Constants.ERROR_ACCOUNT_NOT_FOUND);
        }
        Helper.sendResponseBody(exchange, account);
    }

    /**
     * Retrieves all user accounts
     *
     * @param exchange Client connection object
     */
    public void getAll(HttpServerExchange exchange) {
        Collection<Account> accountList = accountService.getAll();
        Helper.sendResponseBody(exchange, accountList);
    }

    private static void validate(Account account) throws ApiException {
        if (account.getNumber() == null || account.getNumber().isEmpty()) {
            throw new ApiException(400, Constants.ERROR_INVALID_REQUEST);
        }
    }

}
