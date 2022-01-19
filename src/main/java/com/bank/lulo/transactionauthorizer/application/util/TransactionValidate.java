package com.bank.lulo.transactionauthorizer.application.util;

import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.AccountRepository;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.Transaction;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionValidate {

    private List<String> violations = new ArrayList<>();
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private final String MESSAGE_ACCOUNT_NOT_INITIALIZED = "account_not_initialized";

    public List<String> validateTransaction(Transaction transaction){
        clearViolations();
        validateAccountInitialized(transaction);
        return violations;
    }

    private void validateAccountInitialized(Transaction transaction) {
        Account account = accountRepository.findById(transaction.getIdAccount());
        if(account == null){
            violations.add(MESSAGE_ACCOUNT_NOT_INITIALIZED);
        }
    }


    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private void clearViolations(){
        this.violations = new ArrayList<>();
    }
}
