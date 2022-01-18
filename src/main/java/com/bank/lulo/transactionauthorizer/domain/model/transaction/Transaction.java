package com.bank.lulo.transactionauthorizer.domain.model.transaction;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Transaction implements Serializable {

    @NotEmpty(message = "id empty")
    private int idAccount;

    @NotEmpty(message = "merchant empty")
    private String merchant;

    @NotEmpty(message = "amount empty")
    private int idCuenta;

    @NotNull(message = "time empty")
    private LocalDateTime time;

}
