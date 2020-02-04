package com.github.wladox.moneytransfer.model;

import lombok.Data;

import java.util.List;

@Data
public class TransferHistoryResponse {

    private List<TransferRecord> history;
}
