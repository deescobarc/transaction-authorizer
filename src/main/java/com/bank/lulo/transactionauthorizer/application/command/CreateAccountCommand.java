package com.bank.lulo.transactionauthorizer.application.command;

import com.bank.lulo.transactionauthorizer.application.commandbus.Command;
import com.bank.lulo.transactionauthorizer.domain.model.account.Account;

public class CreateAccountCommand extends Command {

    private Account account;

    public CreateAccountCommand(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public static class  Builder {

        private Account account;

        public static Builder getInstance() {
            return new Builder();
        }

        public Builder account(Account account){
            this.account = account;
            return this;
        }

        public CreateAccountCommand build(){
            return new CreateAccountCommand(account);
        }
    }
}
