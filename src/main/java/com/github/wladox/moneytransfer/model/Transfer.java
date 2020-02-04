package com.github.wladox.moneytransfer.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Builder
@Data
public class Transfer {

    Long id;
    String from;
    String to;
    BigDecimal amount;
    Date created;

}
