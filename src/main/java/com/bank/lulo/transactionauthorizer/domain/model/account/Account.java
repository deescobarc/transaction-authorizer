package com.bank.lulo.transactionauthorizer.domain.model.account;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEventCollection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.nashorn.internal.objects.annotations.Property;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class Account implements Serializable {

    @NotEmpty(message = "id empty")
    private final int id;

    @NotEmpty(message = "active-card empty")
    @JsonProperty("active-card")
    private boolean activeCard;

    @Property(name = "available-limit")
    @JsonProperty("available-limit")
    @NotEmpty(message = "available-limit empty")
    private int availableLimit;

    @JsonIgnore
    private DomainEventCollection domainEvents;

    @JsonIgnore
    private List<String> violations = new ArrayList<>();

    public Account(int id, boolean activeCard, int availableLimit, DomainEventCollection domainEventCollection) {
        this.id = id;
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
        this.domainEvents = domainEventCollection;
    }

    public static Account create(int id, boolean activeCard, int availableLimit){

        Account account = new Account(id, activeCard, availableLimit, new DomainEventCollection());
        account.domainEvents.add(new AccountCreatedDomainEvent(account.getId()));
        return account;
    }

    public static Account updateWithViolations(Account account, List<String> violations){

        account.setViolations(violations);
        account.domainEvents.add(new AccountViolationDomainEvent(account.getId()));
        return account;
    }

    public static Account updateAmmount(Account account, int amount){

        int newAvailableLimit = account.getAvailableLimit() - amount;
        account.setAvailableLimit(newAvailableLimit);
        account.setViolations(new ArrayList<>());
        account.domainEvents.add(new AccountUpdateAvailableLimitDomainEvent(account.getId()));
        return account;
    }

}
