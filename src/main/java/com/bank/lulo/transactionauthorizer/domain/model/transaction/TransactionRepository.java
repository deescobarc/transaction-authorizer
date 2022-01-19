package com.bank.lulo.transactionauthorizer.domain.model.transaction;


public interface TransactionRepository {

    int getId();

    Transaction findById(TransactionId id);

    void persist(Transaction transaction);
}
