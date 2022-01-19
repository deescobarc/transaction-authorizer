package com.bank.lulo.transactionauthorizer.application.util;

import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountValidate {

    private List<String> violations = new ArrayList<>();
    private AccountRepository accountRepository;
    private final String MESSAGE_EXISTS_ACCOUNT = "account-already-initialized";

    public List<String> validateAccount(Account account) {
        clearViolations();
        validateExistence(account);
        return violations;
    }

    private void validateExistence(Account account) {
        Account accountExists = accountRepository.findById(account.getId());
        if(accountExists != null){
            violations.add(MESSAGE_EXISTS_ACCOUNT);
        }
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private void clearViolations(){
        this.violations = new ArrayList<>();
    }
}
