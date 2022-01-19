package com.bank.lulo.transactionauthorizer.application.command;

import com.bank.lulo.transactionauthorizer.application.commandbus.CommandHandler;
import com.bank.lulo.transactionauthorizer.application.usecases.AuthorizeTransactionUseCase;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeTransactionCommandHandler implements CommandHandler<AuthorizeTransactionCommand> {

    private TransactionRepository transactionRepository;
    private AuthorizeTransactionUseCase useCase;

    public AuthorizeTransactionCommandHandler(TransactionRepository transactionRepository, AuthorizeTransactionUseCase authorizeTransactionUseCase) {
        this.transactionRepository = transactionRepository;
        this.useCase = authorizeTransactionUseCase;
    }

    @Override
    public void handle(AuthorizeTransactionCommand command) throws Exception {
        System.out.println("Authorizing transaction");
        useCase.handle(command.getTransaction().getIdTransaction(),command.getTransaction().getIdAccount(), command.getTransaction().getMerchant(), command.getTransaction().getAmount(), command.getTransaction().getTime());
    }
}
