package com.github.wladox.moneytransfer.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Builder
@Data
public class Account {

    public static final String COL_ID       = "id";
    public static final String COL_NUMBER   = "number";
    public static final String COL_BALANCE  = "balance";

    private String id;
    private String number;
    private BigDecimal balance = BigDecimal.ZERO;

}
