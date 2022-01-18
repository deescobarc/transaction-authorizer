package com.bank.lulo.transactionauthorizer;

import com.bank.lulo.transactionauthorizer.application.commandbus.CommandBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionAuthorizerApplication implements CommandLineRunner {

	@Autowired
	private CommandBus commandBus;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Starting commandBus");
	}

	public static void main(String[] args) {
		SpringApplication.run(TransactionAuthorizerApplication.class, args);
	}

}
