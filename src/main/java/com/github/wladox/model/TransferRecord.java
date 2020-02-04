package com.github.wladox.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;


@Builder
@Data
public class TransferRecord {

    private Long id;
    private Date created;
    private Transfer transfer;

}
