package com.bank.lulo.transactionauthorizer.domain.model.transaction;

import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import lombok.Data;

@Data
public class RequestTransaction {

    private Transaction transaction;
    private Account account;
}
