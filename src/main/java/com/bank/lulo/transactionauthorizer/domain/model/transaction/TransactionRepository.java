package com.bank.lulo.transactionauthorizer.domain.model.transaction;


import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository {

    int getId();

    Transaction findById(TransactionId id);

    List<Transaction> getTransactionsInIntervalForAccount(LocalDateTime timeA, LocalDateTime timeB, int idAccount);

    List<Transaction> getTransactionsInIntervalForAmountAndMerchant(LocalDateTime timeA, LocalDateTime timeB, int amount, String merchant);

    void persist(Transaction transaction);
}
