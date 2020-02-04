package com.github.wladox.moneytransfer.controller;

import com.github.wladox.moneytransfer.exceptions.AccountNotFoundException;
import com.github.wladox.moneytransfer.exceptions.TransactionException;
import com.github.wladox.moneytransfer.model.Transfer;
import com.github.wladox.moneytransfer.model.TransferRecord;
import com.github.wladox.moneytransfer.service.MoneyTransferService;
import com.github.wladox.moneytransfer.util.Helper;
import io.undertow.server.HttpServerExchange;

import java.util.Collection;

public class TransfersController {

    private MoneyTransferService transferService;

    public TransfersController(MoneyTransferService service) {
        this.transferService = service;
    }

    public void create(HttpServerExchange exchange) throws TransactionException, AccountNotFoundException {
        Transfer transfer = Helper.getFrom(exchange, Transfer.class);
        Long txId = transferService.process(transfer);
        exchange.setStatusCode(201);
        Helper.sendResponseBody(exchange, txId);
    }

    public void getAll(HttpServerExchange exchange) {
        String  id = Helper.getAccountId(exchange);
        Collection<TransferRecord> history = transferService.showHistory(id);
        exchange.setStatusCode(200);
        Helper.sendResponseBody(exchange, history);
    }

}
