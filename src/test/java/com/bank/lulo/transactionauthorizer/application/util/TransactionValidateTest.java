package com.bank.lulo.transactionauthorizer.application.util;

import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.AccountRepository;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.Transaction;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionRepository;
import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEventCollection;
import junitparams.JUnitParamsRunner;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class TransactionValidateTest {

    TransactionValidate transactionValidate;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AccountRepository accountRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        transactionValidate = new TransactionValidate();
        transactionValidate.setTransactionRepository(transactionRepository);
        transactionValidate.setAccountRepository(accountRepository);
    }

    @Test
    public void validate_transaction_when_account_initialized() {
        Transaction transaction = new Transaction(1, "Burger King", 20, "2019-02-13T10:00:00.000Z");
        when(accountRepository.findById(anyInt())).thenReturn(null);
        List<String> violations =  transactionValidate.validateTransaction(transaction);
        Assert.assertTrue(violations.contains("account_not_initialized"));
    }

    @Test
    public void validate_transaction_when_card_not_active() {
        Transaction transaction = new Transaction(1, "Burger King", 20, "2019-02-13T10:00:00.000Z");
        Account account = new Account(1, false, 100, new DomainEventCollection());
        when(accountRepository.findById(anyInt())).thenReturn(account);
        List<String> violations =  transactionValidate.validateTransaction(transaction);
        Assert.assertTrue(violations.contains("card-not-active"));
    }

    @Test
    public void validate_transaction_when_insufficient_limit() {
        Transaction transaction = new Transaction(1, "Burger King", 200, "2019-02-13T10:00:00.000Z");
        Account account = new Account(1, false, 100, new DomainEventCollection());
        when(accountRepository.findById(anyInt())).thenReturn(account);
        List<String> violations =  transactionValidate.validateTransaction(transaction);
        Assert.assertTrue(violations.contains("insufficient-limit"));
    }

    @Test
    public void validate_transaction_high_frequency_small_interval_before() {
        Transaction transaction_1 = new Transaction(1, "Burger King", 200, "2019-02-13T10:00:00.000Z");
        Transaction transaction_2 = new Transaction(1, "Burger King", 200, "2019-02-13T10:00:01.000Z");
        Transaction transaction_3 = new Transaction(1, "Burger King", 200, "2019-02-13T10:00:02.000Z");
        Account account = new Account(1, false, 100, new DomainEventCollection());
        List<Transaction> transactionsBefore = new ArrayList<>();
        transactionsBefore.add(transaction_1);
        transactionsBefore.add(transaction_2);

        when(accountRepository.findById(anyInt())).thenReturn(account);
        when(transactionRepository.getTransactionsInIntervalForAccount(any(), any(), anyInt())).thenReturn(transactionsBefore);
        List<String> violations =  transactionValidate.validateTransaction(transaction_3);
        Assert.assertTrue(violations.contains("high-frequency-small-interval"));
    }

    @Test
    public void validate_transaction_high_frequency_small_interval_after() {
        Transaction transaction_1 = new Transaction(1, "Burger King", 200, "2019-02-13T10:00:02.000Z");
        Transaction transaction_2 = new Transaction(1, "Burger King", 200, "2019-02-13T10:00:01.000Z");
        Transaction transaction_3 = new Transaction(1, "Burger King", 200, "2019-02-13T10:00:00.000Z");
        Account account = new Account(1, false, 100, new DomainEventCollection());
        List<Transaction> transactionsAfter = new ArrayList<>();
        transactionsAfter.add(transaction_1);
        transactionsAfter.add(transaction_2);

        when(accountRepository.findById(anyInt())).thenReturn(account);
        when(transactionRepository.getTransactionsInIntervalForAccount(any(), any(), anyInt())).thenReturn(transactionsAfter);
        List<String> violations =  transactionValidate.validateTransaction(transaction_3);
        Assert.assertTrue(violations.contains("high-frequency-small-interval"));
    }

    @Test
    public void validate_transaction_doubled_transaction() {
        Transaction transaction_1 = new Transaction(1, "Burger King", 200, "2019-02-13T10:00:01.000Z");
        Transaction transaction_2 = new Transaction(1, "Burger King", 200, "2019-02-13T10:00:00.000Z");
        Account account = new Account(1, false, 100, new DomainEventCollection());
        List<Transaction> transactionsAfter = new ArrayList<>();
        transactionsAfter.add(transaction_1);
        transactionsAfter.add(transaction_2);

        when(accountRepository.findById(anyInt())).thenReturn(account);
        when(transactionRepository.getTransactionsInIntervalForAmountAndMerchant(any(), any(), anyInt(), any())).thenReturn(transactionsAfter);
        List<String> violations =  transactionValidate.validateTransaction(transaction_2);
        Assert.assertTrue(violations.contains("doubled-transaction"));
    }
}
