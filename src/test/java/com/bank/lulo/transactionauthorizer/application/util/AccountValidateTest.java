package com.bank.lulo.transactionauthorizer.application.util;

import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.AccountRepository;
import junitparams.JUnitParamsRunner;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class AccountValidateTest {

    AccountValidate accountValidate;

    @Mock
    AccountRepository accountRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        accountValidate = new AccountValidate();
        accountValidate.setAccountRepository(accountRepository);
    }

    @Test
    public void validate_existing_account() {
        Account account = new Account();
        account.setId(1);
        when(accountRepository.findById(anyInt())).thenReturn(account);
        List<String> violations =  accountValidate.validateAccount(account);
        Assert.assertTrue(violations.contains("account-already-initialized"));
    }
}
