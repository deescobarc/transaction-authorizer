package com.bank.lulo.transactionauthorizer.domain.model.transaction;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public final class Transaction implements Serializable {

    @NotEmpty(message = "id empty")
    private final int idAccount;

    @NotEmpty(message = "merchant empty")
    private final String merchant;

    @NotEmpty(message = "amount empty")
    private final int idCuenta;

    @NotNull(message = "time empty")
    private final LocalDateTime time;

    public Transaction(int idAccount, String merchant, int idCuenta, LocalDateTime time) {
        this.idAccount = idAccount;
        this.merchant = merchant;
        this.idCuenta = idCuenta;
        this.time = time;
    }
}
