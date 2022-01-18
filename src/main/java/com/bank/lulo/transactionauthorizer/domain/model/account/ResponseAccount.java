package com.bank.lulo.transactionauthorizer.domain.model.account;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseAccount {

    private Account account;
    private List<String> violations = new ArrayList<>();
}
