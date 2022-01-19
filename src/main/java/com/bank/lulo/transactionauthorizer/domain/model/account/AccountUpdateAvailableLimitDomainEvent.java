package com.bank.lulo.transactionauthorizer.domain.model.account;

import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEvent;

public class AccountUpdateAvailableLimitDomainEvent extends DomainEvent  {

    private int accountId;

    public AccountUpdateAvailableLimitDomainEvent(int accountId) {
        this.accountId = accountId;
    }

    public int getAccountId() {
        return accountId;
    }
}
