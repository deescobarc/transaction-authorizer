package com.bank.lulo.transactionauthorizer.application.query;

import com.bank.lulo.transactionauthorizer.application.querybus.QueryHandler;
import com.bank.lulo.transactionauthorizer.application.usecases.GetTransaccionUseCase;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.Transaction;
import org.springframework.stereotype.Component;

@Component
public class GetTransactionQueryHandler implements QueryHandler<Transaction, GetTransactionQuery> {

    private GetTransaccionUseCase useCase;

    public GetTransactionQueryHandler(GetTransaccionUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public Transaction handle(GetTransactionQuery query) throws Exception {
        return useCase.handle(query.getId());
    }
}
