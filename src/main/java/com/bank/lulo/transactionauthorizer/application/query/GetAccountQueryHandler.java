package com.bank.lulo.transactionauthorizer.application.query;

import com.bank.lulo.transactionauthorizer.application.querybus.QueryHandler;
import com.bank.lulo.transactionauthorizer.application.usecases.GetAccountUseCase;
import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import org.springframework.stereotype.Component;

@Component
public class GetAccountQueryHandler implements QueryHandler<Account, GetAccountQuery> {

    private GetAccountUseCase useCase;

    public GetAccountQueryHandler(GetAccountUseCase useCase) {
        this.useCase = useCase;
    }


    @Override
    public Account handle(GetAccountQuery query) throws Exception {
        return useCase.handle(query.getId());
    }
}
