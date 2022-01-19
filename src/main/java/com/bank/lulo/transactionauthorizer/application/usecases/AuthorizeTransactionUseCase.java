package com.bank.lulo.transactionauthorizer.application.usecases;

import com.bank.lulo.transactionauthorizer.application.util.TransactionValidate;
import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.AccountRepository;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.Transaction;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionId;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionRepository;
import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEventBus;
import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEventCollection;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AuthorizeTransactionUseCase {

    private final TransactionValidate transactionValidate;
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private final DomainEventBus domainEventBus;

    public AuthorizeTransactionUseCase(TransactionValidate transactionValidate, TransactionRepository transactionRepository, AccountRepository accountRepository, DomainEventBus domainEventBus) {
        this.transactionValidate = transactionValidate;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.domainEventBus = domainEventBus;
    }

    public void handle(TransactionId id, int idAccount, String merchant, int amount, LocalDateTime time) {

        Transaction transaction = new Transaction(id, idAccount, merchant, amount, time, new DomainEventCollection());
        transactionValidate.setAccountRepository(accountRepository);
        transactionValidate.setTransactionRepository(transactionRepository);

        List<String> violations = transactionValidate.validateTransaction(transaction);

        Account accountSearch = accountRepository.findById(idAccount);

        if(!violations.isEmpty()){
            if(accountSearch != null){
                Account.updateWithViolations(accountSearch, violations);
                domainEventBus.publish(accountSearch.getDomainEvents());
            }else{
                Transaction.updateWithViolations(transaction, violations);
            }
        }else{
            Account.updateAmmount(accountSearch, amount);
            Transaction.create(transaction);
        }

        transactionRepository.persist(transaction);
        domainEventBus.publish(transaction.getDomainEvents());

    }
}
