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
    public void validate_account_not_initialized() {
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

    @Test
    public void validate_account_card_not_active() {
        List<String> listMock = new ArrayList<String>();
        listMock.add("card-not-active");
        LocalDateTime time = ConvertDate.getLocalDateTimeFromString("2019-02-13T10:00:00.000Z");
        Transaction transaction = new Transaction(1,"Burger King",20,time,  new DomainEventCollection());
        transaction.setViolations(listMock);
        Account account = new Account(1, false, 100, new DomainEventCollection());

        when(transactionValidate.validateTransaction(any())).thenReturn(listMock);
        when(accountRepository.findById(anyInt())).thenReturn(account);
        when(transactionRepository.findById(any())).thenReturn(transaction);

        authorizeTransactionUseCase.handle(transaction.getIdTransaction(), 1,"Burger King",20,time);
        Account accountUpdate = getAccountUseCase.handle(1);

        Assert.assertTrue(accountUpdate.getViolations().contains("card-not-active"));
    }

    @Test
    public void validate_account_insufficient_limit() {
        List<String> listMock = new ArrayList<String>();
        listMock.add("insufficient-limit");
        LocalDateTime time = ConvertDate.getLocalDateTimeFromString("2019-02-13T10:00:00.000Z");
        Transaction transaction = new Transaction(1,"Burger King",20,time,  new DomainEventCollection());
        transaction.setViolations(listMock);
        Account account = new Account(1, true, 10, new DomainEventCollection());

        when(transactionValidate.validateTransaction(any())).thenReturn(listMock);
        when(accountRepository.findById(anyInt())).thenReturn(account);
        when(transactionRepository.findById(any())).thenReturn(transaction);

        authorizeTransactionUseCase.handle(transaction.getIdTransaction(), 1,"Burger King",20,time);
        Account accountUpdate = getAccountUseCase.handle(1);

        Assert.assertTrue(accountUpdate.getViolations().contains("insufficient-limit"));
    }

    @Test
    public void validate_high_frequency_small_interval() {
        List<String> listMock = new ArrayList<String>();
        LocalDateTime time_1 = ConvertDate.getLocalDateTimeFromString("2019-02-13T10:00:00.000Z");
        LocalDateTime time_2 = ConvertDate.getLocalDateTimeFromString("2019-02-13T10:00:01.000Z");
        LocalDateTime time_3 = ConvertDate.getLocalDateTimeFromString("2019-02-13T10:00:02.000Z");
        Transaction transaction_1 = new Transaction(1,"Burger King",20,time_1,  new DomainEventCollection());
        Transaction transaction_2 = new Transaction(1,"Burger King",20,time_2,  new DomainEventCollection());
        Transaction transaction_3 = new Transaction(1,"Burger King",20,time_3,  new DomainEventCollection());

        transaction_3.setViolations(listMock);
        Account account = new Account(1, true, 100, new DomainEventCollection());

        when(transactionValidate.validateTransaction(any())).thenReturn(listMock);
        when(accountRepository.findById(anyInt())).thenReturn(account);

        authorizeTransactionUseCase.handle(transaction_1.getIdTransaction(), 1,"Burger King",20,time_1);
        authorizeTransactionUseCase.handle(transaction_2.getIdTransaction(), 1,"Burger King",20,time_2);

        listMock.add("high-frequency-small-interval");
        when(transactionValidate.validateTransaction(any())).thenReturn(listMock);
        when(transactionRepository.findById(any())).thenReturn(transaction_3);

        authorizeTransactionUseCase.handle(transaction_3.getIdTransaction(), 1,"Burger King",20,time_3);
        Account accountUpdate = getAccountUseCase.handle(1);

        Assert.assertTrue(accountUpdate.getViolations().contains("high-frequency-small-interval"));
    }

    @Test
    public void validate_double_transaction() {
        List<String> listMock = new ArrayList<String>();
        LocalDateTime time_1 = ConvertDate.getLocalDateTimeFromString("2019-02-13T10:00:00.000Z");
        LocalDateTime time_2 = ConvertDate.getLocalDateTimeFromString("2019-02-13T10:00:01.000Z");
        Transaction transaction_1 = new Transaction(1,"Burger King",20,time_1,  new DomainEventCollection());
        Transaction transaction_2 = new Transaction(1,"Burger King",20,time_2,  new DomainEventCollection());

        transaction_2.setViolations(listMock);
        Account account = new Account(1, true, 100, new DomainEventCollection());

        when(transactionValidate.validateTransaction(any())).thenReturn(listMock);
        when(accountRepository.findById(anyInt())).thenReturn(account);

        authorizeTransactionUseCase.handle(transaction_1.getIdTransaction(), 1,"Burger King",20,time_1);

        listMock.add("doubled-transaction");
        when(transactionValidate.validateTransaction(any())).thenReturn(listMock);
        when(transactionRepository.findById(any())).thenReturn(transaction_2);

        authorizeTransactionUseCase.handle(transaction_2.getIdTransaction(), 1,"Burger King",20,time_2);
        Account accountUpdate = getAccountUseCase.handle(1);

        Assert.assertTrue(accountUpdate.getViolations().contains("doubled-transaction"));
    }

    @Test
    public void transaction_authorized_success() {
        Account account = new Account(1, true, 100, new DomainEventCollection());
        LocalDateTime time = ConvertDate.getLocalDateTimeFromString("2019-02-13T10:00:00.000Z");
        Transaction transaction = new Transaction(1,"Burger King",20,time,  new DomainEventCollection());
        List<String> listMock = new ArrayList<String>();

        when(transactionValidate.validateTransaction(any())).thenReturn(listMock);
        when(accountRepository.findById(anyInt())).thenReturn(account);
        when(transactionRepository.findById(any())).thenReturn(transaction);

        authorizeTransactionUseCase.handle(transaction.getIdTransaction(), 1,"Burger King",20,time);

        Transaction responseTransaction = getTransaccionUseCase.handle(transaction.getIdTransaction());
        Account accountUpdate = getAccountUseCase.handle(1);

        Assert.assertEquals(transaction, responseTransaction);
        Assert.assertEquals(accountUpdate.getAvailableLimit(), 80);
    }
}
