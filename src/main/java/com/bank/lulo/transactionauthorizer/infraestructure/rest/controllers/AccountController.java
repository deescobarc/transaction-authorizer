package com.bank.lulo.transactionauthorizer.infraestructure.rest.controllers;

import com.bank.lulo.transactionauthorizer.application.command.AuthorizeTransactionCommand;
import com.bank.lulo.transactionauthorizer.application.command.CreateAccountCommand;
import com.bank.lulo.transactionauthorizer.application.commandbus.CommandBus;
import com.bank.lulo.transactionauthorizer.application.query.GetAccountQuery;
import com.bank.lulo.transactionauthorizer.application.query.GetTransactionQuery;
import com.bank.lulo.transactionauthorizer.application.querybus.QueryBus;
import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.RequestAccount;
import com.bank.lulo.transactionauthorizer.domain.model.account.ResponseAccount;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.RequestTransaction;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private CommandBus commandBus;
    private QueryBus queryBus;

    public AccountController(CommandBus commandBus, QueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ResponseAccount> operationsAccountAndTransactions(@RequestBody RequestTransaction requestTransaction) throws Exception {
        ResponseAccount responseAccount = null;
        if(requestTransaction.getAccount() != null){
            responseAccount = createAccount(requestTransaction);

        }else if(requestTransaction.getTransaction() != null){
            responseAccount = authorizeTransaction(requestTransaction);
        }

        if(responseAccount == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(responseAccount);

    }

    private ResponseAccount createAccount(RequestTransaction requestTransaction) throws Exception {
        RequestAccount requestAccount = new RequestAccount(requestTransaction.getAccount());
        CreateAccountCommand createAccountCommand = CreateAccountCommand.Builder.getInstance()
                .account(requestAccount.getAccount())
                .build();
        commandBus.handle(createAccountCommand);

        GetAccountQuery getAccountQueryCommand = GetAccountQuery.Builder.getInstance()
                .id(requestAccount.getAccount().getId())
                .build();
        Account account = queryBus.handle(getAccountQueryCommand);
        if(account == null){
            return null;
        }

        return new ResponseAccount(account);
    }

    private ResponseAccount authorizeTransaction(RequestTransaction requestTransaction) throws Exception {
        AuthorizeTransactionCommand authorizeTransactionCommand = AuthorizeTransactionCommand.Builder.getInstance()
                .transaction(requestTransaction.getTransaction())
                .build();
        commandBus.handle(authorizeTransactionCommand);

        GetAccountQuery getAccountQueryCommand = GetAccountQuery.Builder.getInstance()
                .id(requestTransaction.getTransaction().getIdAccount())
                .build();
        Account account = queryBus.handle(getAccountQueryCommand);
        ResponseAccount responseAccount;
        if(account == null){
            GetTransactionQuery getTransactionQueryCommand = GetTransactionQuery.Builder.getInstance()
                    .id(requestTransaction.getTransaction().getIdTransaction())
                    .build();
            Transaction transaction = queryBus.handle(getTransactionQueryCommand);
            if(transaction == null){
                return null;
            }
            responseAccount = new ResponseAccount(transaction);
        }else{
            responseAccount = new ResponseAccount(account);
        }
        return responseAccount;
    }
}
