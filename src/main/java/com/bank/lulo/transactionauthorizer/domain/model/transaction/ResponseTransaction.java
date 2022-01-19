package com.bank.lulo.transactionauthorizer.domain.model.transaction;

import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseTransaction {

    private Account account;
    private List<String> violations = new ArrayList<>();

    public ResponseTransaction(Transaction transaction){
        this.account = new Account(transaction.getIdAccount(), false, 0, null);
        this.violations = transaction.getViolations();
    }

    public ResponseTransaction(Account account){
        this.account = account;
        this.violations = account.getViolations();
    }
}
