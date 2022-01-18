package com.bank.lulo.transactionauthorizer.infraestructure.rest.controllers;

import com.bank.lulo.transactionauthorizer.application.command.CreateAccountCommand;
import com.bank.lulo.transactionauthorizer.application.commandbus.CommandBus;
import com.bank.lulo.transactionauthorizer.application.query.GetAccountQuery;
import com.bank.lulo.transactionauthorizer.application.querybus.QueryBus;
import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.RequestAccount;
import com.bank.lulo.transactionauthorizer.domain.model.account.ResponseAccount;
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
    public ResponseEntity<ResponseAccount> createAccount(@RequestBody RequestAccount requestAccount) throws Exception {
        CreateAccountCommand createAccountCommand = CreateAccountCommand.Builder.getInstance()
                .account(requestAccount.getAccount())
                .build();
        commandBus.handle(createAccountCommand);

        GetAccountQuery getAccountQueryCommand = GetAccountQuery.Builder.getInstance()
                .id(requestAccount.getAccount().getId())
                .build();
        Account account = queryBus.handle(getAccountQueryCommand);
        if(account == null){
            return ResponseEntity.notFound().build();
        }

        ResponseAccount responseAccount = new ResponseAccount();
        responseAccount.setAccount(account);
        responseAccount.setViolations(account.getViolations());

        return ResponseEntity.ok().body(responseAccount);
    }
}
