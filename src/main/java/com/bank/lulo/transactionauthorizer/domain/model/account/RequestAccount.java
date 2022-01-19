package com.bank.lulo.transactionauthorizer.domain.model.account;

import lombok.Data;

@Data
public class RequestAccount {

    private Account account;

    public RequestAccount(Account account) {
        this.account = account;
    }
}
