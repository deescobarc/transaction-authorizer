package com.bank.lulo.transactionauthorizer.domain.model.transaction;

import com.bank.lulo.transactionauthorizer.application.util.ConvertDate;
import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.AccountCreatedDomainEvent;
import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEventCollection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public final class Transaction implements Serializable {

    @JsonIgnore
    private TransactionId idTransaction;

    @NotEmpty(message = "id")
    @JsonProperty("id")
    private int idAccount;

    @NotEmpty(message = "merchant empty")
    @JsonProperty("merchant")
    private String merchant;

    @NotEmpty(message = "amount empty")
    @JsonProperty("amount")
    private int amount;

    @NotNull(message = "time empty")
    @JsonProperty("time")
    private LocalDateTime time;

    @JsonIgnore
    private DomainEventCollection domainEvents;

    @JsonIgnore
    private List<String> violations = new ArrayList<>();

    public Transaction(){
        this.idTransaction = new TransactionId(UUID.randomUUID());
        this.domainEvents = new DomainEventCollection();
    }

    public Transaction(int id, String merchant, int amount, String time) {
        this.idTransaction = new TransactionId(UUID.randomUUID());
        this.idAccount = id;
        this.merchant = merchant;
        this.amount = amount;
        this.time = ConvertDate.getLocalDateTimeFromString(time);
        this.domainEvents = new DomainEventCollection();
    }

    public Transaction(int idAccount, String merchant, int amount, LocalDateTime time, DomainEventCollection domainEventCollection) {
        this.idTransaction = new TransactionId(UUID.randomUUID());
        this.idAccount = idAccount;
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
        this.domainEvents = domainEventCollection;
    }

    public Transaction(TransactionId idTransaction, int idAccount, String merchant, int amount, LocalDateTime time, DomainEventCollection domainEventCollection) {
        this.idTransaction = idTransaction;
        this.idAccount = idAccount;
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
        this.domainEvents = domainEventCollection;
    }

    public static Transaction updateWithViolations(Transaction transaction, List<String> violations){

        transaction.setViolations(violations);
        transaction.domainEvents.add(new TransactionAccountNotInitiliazedDomainEvent(transaction.getIdTransaction()));
        return transaction;
    }

    public static Transaction create(Transaction transaction){

        transaction.domainEvents.add(new TransactionAuthorizedDomainEvent(transaction.getIdTransaction()));
        return transaction;
    }
}
