package com.bank.lulo.transactionauthorizer.infraestructure;

import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryAccountRepository implements AccountRepository {

    private AtomicLong secuence;
    private Map<Integer, Account> accounts;

    public InMemoryAccountRepository(){
        this.secuence = new AtomicLong();
        this.accounts = new HashMap<>();
    }

    @Override
    public int getId() {
        return (int) secuence.addAndGet(1);
    }

    @Override
    public Account findById(int id) {
        return accounts.get(id);
    }

    @Override
    public void persist(Account account) {
        accounts.put(account.getId(), account);
    }
}
