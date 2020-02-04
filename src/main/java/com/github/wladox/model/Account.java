package com.github.wladox.model;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class Account {


    public static final String COL_ID       = "id";
    public static final String COL_NUMBER   = "number";
    public static final String COL_BALANCE  = "balance";


    private String id;
    private String number;
    private BigDecimal balance = BigDecimal.ZERO;

    public Account setId(String id) {
        this.id = id;
        return this;
    }

    public Account setNumber(String number) {
        this.number = number;
        return this;
    }

    public Account setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }
}
