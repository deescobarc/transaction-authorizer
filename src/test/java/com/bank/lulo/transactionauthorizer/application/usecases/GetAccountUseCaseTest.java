package com.bank.lulo.transactionauthorizer.application.usecases;

import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.AccountRepository;
import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEventCollection;
import junitparams.JUnitParamsRunner;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class GetAccountUseCaseTest {

    Account account;
    GetAccountUseCase getAccountUseCase;

    @Mock
    AccountRepository accountRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        getAccountUseCase = new GetAccountUseCase(accountRepository);
    }

    @Test
    public void get_account_success() {
        account = new Account(1, true, 100, new DomainEventCollection());

        when(accountRepository.findById(anyInt())).thenReturn(account);
        Account responseAccount = getAccountUseCase.handle(1);

        Assert.assertEquals(account, responseAccount);
    }

    @Test
    public void get_null_when_account_does_no_exist() {

        Account responseAccount = getAccountUseCase.handle(1);

        Assert.assertNull(responseAccount);

    }

}
