package com.bank.lulo.transactionauthorizer.application.usecases;

import com.bank.lulo.transactionauthorizer.domain.model.transaction.Transaction;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionId;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class GetTransaccionUseCase {

    private final TransactionRepository transactionRepository;

    public GetTransaccionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction handle(TransactionId transactionId){
        return transactionRepository.findById(transactionId);
    }
}
