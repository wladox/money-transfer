package com.github.wladox.moneytransfer.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Builder
@Data
public class Transfer {

    private Long id;
    private String from;
    private String to;
    private BigDecimal amount;
    private Date created;

}
