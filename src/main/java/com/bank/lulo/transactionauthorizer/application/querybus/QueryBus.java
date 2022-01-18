package com.bank.lulo.transactionauthorizer.application.querybus;

public interface QueryBus {

    <T> T handle(Query<T> query) throws Exception;
}
