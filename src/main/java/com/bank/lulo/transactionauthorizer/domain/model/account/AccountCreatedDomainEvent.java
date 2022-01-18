package com.bank.lulo.transactionauthorizer.domain.model.account;

import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEvent;

public class AccountCreatedDomainEvent extends DomainEvent {

    private int accountId;

    public AccountCreatedDomainEvent(int accountId) {
        this.accountId = accountId;
    }

    public int getAccountId() {
        return accountId;
    }
}
