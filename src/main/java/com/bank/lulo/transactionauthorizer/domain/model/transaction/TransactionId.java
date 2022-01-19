package com.bank.lulo.transactionauthorizer.domain.model.transaction;

import java.util.UUID;

public class TransactionId {

    private UUID id;

    public TransactionId(UUID id) {
        this.id = id;
    }

    public String getValue() {
        return id.toString();
    }
}
