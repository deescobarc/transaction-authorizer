package com.bank.lulo.transactionauthorizer.domain.model.account;

public interface AccountRepository {

    int getId();

    Account findById(int id);

    void persist(Account account);
}
