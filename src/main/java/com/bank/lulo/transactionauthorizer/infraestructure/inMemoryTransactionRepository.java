package com.bank.lulo.transactionauthorizer.infraestructure;

import com.bank.lulo.transactionauthorizer.domain.model.transaction.Transaction;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionId;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
    public void persist(Transaction transaction) {
        transactions.put(transaction.getIdTransaction(), transaction);
    }
}
