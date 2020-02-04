package com.github.wladox.moneytransfer.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class BalanceUpdateRequest {

    BigDecimal amount;
    BalanceOperation operation;

}
