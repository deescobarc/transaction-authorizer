package com.bank.lulo.transactionauthorizer.infraestructure;

import com.bank.lulo.transactionauthorizer.domain.model.transaction.Transaction;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionId;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class inMemoryTransactionRepository implements TransactionRepository {

    private AtomicLong sequence;
    private Map<TransactionId, Transaction> transactions;

    public inMemoryTransactionRepository() {
        this.sequence = new AtomicLong();
        this.transactions = new HashMap<>();
    }

    @Override
    public int getId() {
        return (int) sequence.addAndGet(1);
    }

    @Override
    public Transaction findById(TransactionId id) {
        return transactions.get(id);
    }

    @Override
    public List<Transaction> getTransactionsInIntervalForAccount(LocalDateTime timeA, LocalDateTime timeB, int idAccount) {
        List<Transaction> transactionsInInterval = new ArrayList<>();
        for (Transaction value : transactions.values()) {
            if(value.getIdAccount() == idAccount
                    && (value.getTime().isAfter(timeA) || value.getTime().isEqual(timeA))
                    && (value.getTime().isBefore(timeB) || value.getTime().isEqual(timeB))){
                transactionsInInterval.add(value);
            }
        }
        return transactionsInInterval;
    }

    @Override
    public List<Transaction> getTransactionsInIntervalForAmountAndMerchant(LocalDateTime timeA, LocalDateTime timeB, int amount, String merchant) {
        List<Transaction> transactionsInInterval = new ArrayList<>();
        for (Transaction value : transactions.values()) {
            if(value.getAmount() == amount && value.getMerchant().equals(merchant)
                    && (value.getTime().isAfter(timeA) || value.getTime().isEqual(timeA))
                    && (value.getTime().isBefore(timeB) || value.getTime().isEqual(timeB))){
                transactionsInInterval.add(value);
            }
        }
        return transactionsInInterval;
    }

    @Override
    public void persist(Transaction transaction) {
        transactions.put(transaction.getIdTransaction(), transaction);
    }
}
