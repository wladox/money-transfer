package com.github.wladox.moneytransfer.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;


@Builder
@Data
public class TransferRecord {

    private String id;
    private Date created;
    private Transfer transfer;

}
