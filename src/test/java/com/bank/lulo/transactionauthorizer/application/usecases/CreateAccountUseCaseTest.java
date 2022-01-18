package com.bank.lulo.transactionauthorizer.application.usecases;

import com.bank.lulo.transactionauthorizer.application.util.AccountValidate;
import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.AccountRepository;
import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEventBus;
import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEventCollection;
import junitparams.JUnitParamsRunner;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnitParamsRunner.class)
public class CreateAccountUseCaseTest {

    CreateAccountUseCase createAccountUseCase;
    Account account;
    GetAccountUseCase getAccountUseCase;

    @Mock
    AccountRepository accountRepository;

    @Mock
    AccountValidate accountValidate;

    @Mock
    DomainEventBus domainEventBus;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        createAccountUseCase = new CreateAccountUseCase(accountValidate, accountRepository, domainEventBus);
        getAccountUseCase = new GetAccountUseCase(accountRepository);
    }

    @Test
    public void account_already_initialized () throws Exception {
        List<String> listMock = new ArrayList<String>();
        listMock.add("account-already-initialized");
        account = new Account(1, true, 100, new DomainEventCollection());

        when(accountValidate.validateAccount(any())).thenReturn(listMock);
        when(accountRepository.findById(anyInt())).thenReturn(account);

        createAccountUseCase.handle(1, true, 100);
        Account responseAccount = getAccountUseCase.handle(1);

        Assert.assertTrue(responseAccount.getViolations().contains("account-already-initialized"));
    }

    @Test
    public void account_created_success() throws Exception {
        account = new Account(1, true, 100, new DomainEventCollection());

        createAccountUseCase.handle(1, true, 100);

        when(accountRepository.findById(anyInt())).thenReturn(account);
        Account responseAccount = getAccountUseCase.handle(1);

        Assert.assertEquals(account, responseAccount);

    }

}
