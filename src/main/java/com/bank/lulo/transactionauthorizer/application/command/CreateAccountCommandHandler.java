package com.bank.lulo.transactionauthorizer.application.command;

import com.bank.lulo.transactionauthorizer.application.commandbus.CommandHandler;
import com.bank.lulo.transactionauthorizer.application.usecases.CreateAccountUseCase;
import com.bank.lulo.transactionauthorizer.domain.model.account.AccountRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateAccountCommandHandler implements CommandHandler<CreateAccountCommand> {

    private AccountRepository accountRepository;
    private CreateAccountUseCase useCase;

    public CreateAccountCommandHandler(AccountRepository accountRepository, CreateAccountUseCase useCase) {
        this.accountRepository = accountRepository;
        this.useCase = useCase;
    }

    @Override
    public void handle(CreateAccountCommand command) throws Exception {
        System.out.println("Creating account");
        useCase.handle(command.getAccount().getId(), command.getAccount().isActiveCard(), command.getAccount().getAvailableLimit());
    }
}
