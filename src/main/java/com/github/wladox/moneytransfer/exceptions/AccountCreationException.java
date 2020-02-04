package com.github.wladox.moneytransfer.exceptions;

public class AccountCreationException extends ApiException {


    public AccountCreationException(String message) {
        super(400, message);
    }
}
