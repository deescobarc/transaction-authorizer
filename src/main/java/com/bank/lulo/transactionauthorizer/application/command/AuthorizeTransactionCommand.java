package com.bank.lulo.transactionauthorizer.application.command;

import com.bank.lulo.transactionauthorizer.application.commandbus.Command;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.Transaction;

public class AuthorizeTransactionCommand extends Command {

    private Transaction transaction;

    public AuthorizeTransactionCommand(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public static class  Builder {

        private Transaction transaction;

        public static AuthorizeTransactionCommand.Builder getInstance() {
            return new AuthorizeTransactionCommand.Builder();
        }

        public AuthorizeTransactionCommand.Builder transaction(Transaction transaction){
            this.transaction = transaction;
            return this;
        }

        public AuthorizeTransactionCommand build(){
            return new AuthorizeTransactionCommand(transaction);
        }
    }
}
