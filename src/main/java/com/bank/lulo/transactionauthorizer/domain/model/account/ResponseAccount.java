package com.bank.lulo.transactionauthorizer.domain.model.account;

import com.bank.lulo.transactionauthorizer.domain.model.transaction.Transaction;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseAccount {

    private Account account;
    private List<String> violations = new ArrayList<>();

    public ResponseAccount(Transaction transaction){
        this.account = new Account(transaction.getIdAccount(), false, 0, null);
        this.violations = transaction.getViolations();
    }

    public ResponseAccount(Account account){
        this.account = account;
        this.violations = account.getViolations();
    }
}
