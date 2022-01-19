package com.bank.lulo.transactionauthorizer.application.query;

import com.bank.lulo.transactionauthorizer.application.querybus.Query;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.Transaction;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionId;

public class GetTransactionQuery extends Query<Transaction> {

    private TransactionId id;

    public GetTransactionQuery(TransactionId id) {
        this.id = id;
    }

    public TransactionId getId() {
        return id;
    }

    public static class Builder {

        private TransactionId id;

        public static GetTransactionQuery.Builder getInstance() {
            return new GetTransactionQuery.Builder();
        }

        public GetTransactionQuery.Builder id(TransactionId id){
            this.id = id;
            return this;
        }

        public GetTransactionQuery build() { return new GetTransactionQuery(id); }

    }
}
