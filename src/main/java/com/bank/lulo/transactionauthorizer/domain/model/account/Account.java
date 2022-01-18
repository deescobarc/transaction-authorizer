package com.bank.lulo.transactionauthorizer.domain.model.account;

import javax.validation.constraints.NotEmpty;

import com.bank.lulo.transactionauthorizer.domain.shared.domaineventbus.DomainEventCollection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.nashorn.internal.objects.annotations.Property;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Account implements Serializable {

    @NotEmpty(message = "id empty")
    private int id;

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

    public static Account create(int id, boolean activeCard, int availableLimit){

        Account account = new Account();
        account.setId(id);
        account.setActiveCard(activeCard);
        account.setAvailableLimit(availableLimit);
        account.setDomainEvents(new DomainEventCollection());

        account.domainEvents.add(new AccountCreatedDomainEvent(account.getId()));
        return account;
    }

    public static Account updateWithViolations(Account account, List<String> violations){

        account.setViolations(violations);
        account.domainEvents.add(new AccountViolationDomainEvent(account.getId()));
        return account;
    }

}
