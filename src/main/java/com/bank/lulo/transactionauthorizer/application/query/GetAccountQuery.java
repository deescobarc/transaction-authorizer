package com.bank.lulo.transactionauthorizer.application.query;

import com.bank.lulo.transactionauthorizer.application.querybus.Query;
import com.bank.lulo.transactionauthorizer.domain.model.account.Account;

public class GetAccountQuery extends Query<Account> {

    private int id;

    public GetAccountQuery(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static class Builder {

        private int id;

        public static GetAccountQuery.Builder getInstance() {
            return new GetAccountQuery.Builder();
        }

        public GetAccountQuery.Builder id(int id){
            this.id = id;
            return this;
        }

        public GetAccountQuery build() { return new GetAccountQuery(id); }

    }
}
