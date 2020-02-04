package com.github.wladox.exceptions;


import com.github.wladox.util.Helper;
import io.undertow.server.HttpServerExchange;

public class ExceptionHandler {

    public static void handleApiException(HttpServerExchange exchange) {
        ApiException ex = (ApiException) exchange.getAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE);
        exchange.setStatusCode(ex.getStatusCode());
        exchange.getResponseSender().send(getBody(ex));
    }

    public static void handleNotFoundException(HttpServerExchange exchange) {
        AccountNotFoundException ex = (AccountNotFoundException) exchange.getAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE);
        exchange.setStatusCode(404);
        exchange.getResponseSender().send(getBody(ex));
    }

    public static void handleCreationException(HttpServerExchange exchange) {
        AccountCreationException ex = (AccountCreationException) exchange.getAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE);
        exchange.setStatusCode(409);
        exchange.getResponseSender().send(getBody(ex));
    }

    public static void handleTransactionException(HttpServerExchange exchange) {
        TransactionException ex = (TransactionException) exchange.getAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE);
        exchange.setStatusCode(400);
        exchange.getResponseSender().send(getBody(ex));
    }

    private static String getBody(Exception ex) {
        return Helper.toJson(new ErrorResponse(ex.getMessage()));
    }

}
