package com.github.wladox.controller;

import com.github.wladox.exceptions.AccountNotFoundException;
import com.github.wladox.exceptions.TransactionException;
import com.github.wladox.model.Transfer;
import com.github.wladox.model.TransferRecord;
import com.github.wladox.service.MoneyTransferService;
import com.github.wladox.util.Helper;
import io.undertow.server.HttpServerExchange;

import java.util.Collection;

import static com.github.wladox.util.Helper.sendResponseBody;

public class TransfersController {

    private MoneyTransferService transferService;

    public TransfersController(MoneyTransferService service) {
        this.transferService = service;
    }

    public void create(HttpServerExchange exchange) throws TransactionException, AccountNotFoundException {
        Transfer transfer = Helper.getFrom(exchange, Transfer.class);
        Long txId = transferService.process(transfer);
        exchange.setStatusCode(201);
        sendResponseBody(exchange, txId);
    }

    public void getAll(HttpServerExchange exchange) {
        String  id = Helper.getAccountId(exchange);
        Collection<TransferRecord> history = transferService.showHistory(id);
        exchange.setStatusCode(200);
        sendResponseBody(exchange, history);
    }

}
