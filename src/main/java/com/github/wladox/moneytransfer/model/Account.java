package com.github.wladox.moneytransfer.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Builder
@Data
public class Account {

    private String id;
    private String number;
    private BigDecimal balance;

}
