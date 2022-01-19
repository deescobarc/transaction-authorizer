package com.bank.lulo.transactionauthorizer.application.usecases;

import com.bank.lulo.transactionauthorizer.application.util.ConvertDate;
import com.bank.lulo.transactionauthorizer.application.util.TransactionValidate;
import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.AccountRepository;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.Transaction;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionRepository;
import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEventBus;
import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEventCollection;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class AuthorizeTransactionUseCaseTest {

    AuthorizeTransactionUseCase authorizeTransactionUseCase;
    Transaction transaction;
    GetAccountUseCase getAccountUseCase;
    GetTransaccionUseCase getTransaccionUseCase;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionValidate transactionValidate;

    @Mock
    DomainEventBus domainEventBus;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        authorizeTransactionUseCase = new AuthorizeTransactionUseCase(transactionValidate, transactionRepository, accountRepository, domainEventBus);
        getAccountUseCase = new GetAccountUseCase(accountRepository);
        getTransaccionUseCase = new GetTransaccionUseCase(transactionRepository);
    }

    @Test
    public void account_not_initialized() {
        List<String> listMock = new ArrayList<String>();
        listMock.add("account-not-initialized");
        LocalDateTime time = ConvertDate.getLocalDateTimeFromString("2019-02-13T10:00:00.000Z");
        Transaction transaction = new Transaction(1,"Burger King",20,time,  new DomainEventCollection());
        transaction.setViolations(listMock);

        when(transactionValidate.validateTransaction(any())).thenReturn(listMock);
        when(accountRepository.findById(anyInt())).thenReturn(null);
        when(transactionRepository.findById(any())).thenReturn(transaction);

        authorizeTransactionUseCase.handle(transaction.getIdTransaction(), 1,"Burger King",20,time);
        Transaction responseTransaction = getTransaccionUseCase.handle(transaction.getIdTransaction());

        Assert.assertTrue(responseTransaction.getViolations().contains("account-not-initialized"));
    }
}
