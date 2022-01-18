package com.bank.lulo.transactionauthorizer.application.usecases;

import com.bank.lulo.transactionauthorizer.application.util.AccountValidate;
import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.AccountRepository;
import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEventBus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateAccountUseCase {

    private AccountValidate accountValidate;
    private AccountRepository accountRepository;
    private DomainEventBus domainEventBus;

    public CreateAccountUseCase(AccountValidate accountValidate, AccountRepository accountRepository, DomainEventBus domainEventBus) {
        this.accountValidate = accountValidate;
        this.accountRepository = accountRepository;
        this.domainEventBus = domainEventBus;
    }

    public void handle(int id, boolean activeCard, int availableLimit) throws Exception {

        Account account = new Account();
        account.setId(id);
        accountValidate.setAccountRepository(accountRepository);

        List<String> violations = accountValidate.validateAccount(account);
        Account accountSearch = accountRepository.findById(id);

        if(!violations.isEmpty()){
            account = Account.updateWithViolations(accountSearch, violations);
        }else{
            account = Account.create(id, activeCard, availableLimit);
        }

        accountRepository.persist(account);
        domainEventBus.publish(account.getDomainEvents());

    }
}
