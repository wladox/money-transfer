package com.github.wladox.moneytransfer.controller;

import com.github.wladox.moneytransfer.Constants;
import com.github.wladox.moneytransfer.exceptions.ApiException;
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

    public void create(HttpServerExchange exchange) throws ApiException, TransactionException {
        Transfer transfer = Helper.getFrom(exchange, Transfer.class);
        validateTransfer(transfer);
        String txId = transferService.process(transfer);
        exchange.setStatusCode(201);
        Helper.sendResponseBody(exchange, txId);
    }

    public void getByNumber(HttpServerExchange exchange) throws ApiException {
        String accountId = Helper.getQueryParam(exchange, "accountId");
        if (accountId == null) {
            throw new ApiException(400, Constants.ERROR_INVALID_REQUEST);
        }
        Collection<TransferRecord> history = transferService.showHistory(accountId);
        exchange.setStatusCode(200);
        Helper.sendResponseBody(exchange, history);
    }

    private static void validateTransfer(Transfer t) throws ApiException {
        if (t.getAmount() == null || t.getFrom() == null || t.getTo() == null) {
            throw new ApiException(400, Constants.ERROR_INVALID_REQUEST);
        }
    }

}
