package com.bank.lulo.transactionauthorizer.application.commandbus;

public interface CommandBus {

    void handle(Command command) throws Exception;
}
