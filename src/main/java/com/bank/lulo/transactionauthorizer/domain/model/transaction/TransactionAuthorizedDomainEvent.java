package com.bank.lulo.transactionauthorizer.domain.model.transaction;

import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEvent;

public class TransactionAuthorizedDomainEvent extends DomainEvent {

    private final TransactionId transactionId;

    public TransactionAuthorizedDomainEvent(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }
}
