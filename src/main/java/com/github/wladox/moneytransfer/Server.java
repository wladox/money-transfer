package com.github.wladox.moneytransfer;

import com.github.wladox.moneytransfer.config.AppConfig;
import com.github.wladox.moneytransfer.controller.AccountsController;
import com.github.wladox.moneytransfer.controller.TransfersController;
import com.github.wladox.moneytransfer.exceptions.*;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.SetHeaderHandler;
import io.undertow.util.Headers;

public class Server {

    private static final String CONTENT_TYPE_VALUE = "application/json";
    public static final String ENDPOINT_ACCOUNTS = "/api/accounts";
    public static final String ENDPOINT_TRANSFERS = "/api/transfers";

    private Undertow server;

    public void buildAndStartServer(AppConfig config,
                                    AccountsController accountsController,
                                    TransfersController transfersHandler) {

        server = Undertow.builder()
                .addHttpListener(config.getPort(), config.getHost())
                .setWorkerThreads(config.getNumberOfThreads())
                .setHandler(new SetHeaderHandler(Handlers.exceptionHandler(
                        Handlers.path()
                                .addPrefixPath("/ping", exchange -> exchange.getResponseSender().send("Pong"))
                                .addPrefixPath(ENDPOINT_ACCOUNTS,
                                        Handlers.routing()
                                                .post("", accountsController::create)
                                                .get("", accountsController::getAll)
                                                .get("/{accountId}", accountsController::get))
                                .addPrefixPath(ENDPOINT_TRANSFERS,
                                        Handlers.routing()
                                                .post("", transfersHandler::create)
                                                .get("", transfersHandler::create)
                                                .get("/log", transfersHandler::getByNumber))
                ).addExceptionHandler(ApiException.class, ExceptionHandler::handleApiException)
                        .addExceptionHandler(AccountCreationException.class, ExceptionHandler::handleCreationException)
                        .addExceptionHandler(AccountNotFoundException.class, ExceptionHandler::handleNotFoundException)
                        .addExceptionHandler(TransactionException.class, ExceptionHandler::handleTransactionException),

                Headers.CONTENT_TYPE.toString(), CONTENT_TYPE_VALUE))
                .build();

        server.start();

    }

    public void shutDown() {
        server.stop();
    }
}
