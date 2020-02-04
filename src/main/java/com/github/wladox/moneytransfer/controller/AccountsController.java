package com.github.wladox.moneytransfer.controller;

import com.github.wladox.moneytransfer.exceptions.AccountNotFoundException;
import com.github.wladox.moneytransfer.exceptions.TransactionException;
import com.github.wladox.moneytransfer.model.Account;
import com.github.wladox.moneytransfer.model.BalanceUpdateRequest;
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
    public void create(HttpServerExchange exchange) {
        Account account = Helper.getFrom(exchange, Account.class);
        accountService.create(account);
        exchange.setStatusCode(201);
    }

    /**
     * Updates a user account
     *
     * @param exchange Client connection object
     */
    public void update(HttpServerExchange exchange) throws AccountNotFoundException, TransactionException {
        String id = Helper.getAccountId(exchange);
        Account account = accountService.getByNumber(id);

        BalanceUpdateRequest request = Helper.getFrom(exchange, BalanceUpdateRequest.class);
        /*switch (request.getOperation()) {
            case DEPOSIT:
                account.deposit(request.getAmount());
                break;
            case WITHDRAW:
                account.withdraw(request.getAmount());
                break;
            default:
                throw new ApiException(400, "Unsupported operation");
        }*/
        exchange.setStatusCode(200);
        Helper.sendResponseBody(exchange, account);
    }

    /**
     * Retrieves a single user account
     *
     * @param exchange Client connection object
     */
    public void get(HttpServerExchange exchange) throws AccountNotFoundException {
        String id = Helper.getAccountId(exchange);
        Account account = accountService.getByNumber(id);
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



}
