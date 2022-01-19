package com.bank.lulo.transactionauthorizer.application.util;

import com.bank.lulo.transactionauthorizer.domain.model.account.Account;
import com.bank.lulo.transactionauthorizer.domain.model.account.AccountRepository;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.Transaction;
import com.bank.lulo.transactionauthorizer.domain.model.transaction.TransactionRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionValidate {

    private List<String> violations = new ArrayList<>();
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private final String MESSAGE_ACCOUNT_NOT_INITIALIZED = "account_not_initialized";
    private final String MESSAGE_CARD_NOT_ACTIVE = "card-not-active";
    private final String MESSAGE_INSUFFICIENT_LIMIT = "insufficient-limit";
    private final String MESSAGE_HIGH_FREQUENCY_SMALL_INTERVAL = "high-frequency-small-interval";
    private final String MESSAGE_DOUBLED_TRANSACTION = "doubled-transaction";

    public List<String> validateTransaction(Transaction transaction){
        clearViolations();
        validateAccountInitialized(transaction);
        validateCardNotActive(transaction);
        validateInsufficientLimit(transaction);
        validateHighFrequencySmallInterval(transaction);
        validateDoubledTransaction(transaction);
        return violations;
    }

    private void validateAccountInitialized(Transaction transaction) {
        Account account = accountRepository.findById(transaction.getIdAccount());
        if(account == null){
            violations.add(MESSAGE_ACCOUNT_NOT_INITIALIZED);
        }
    }

    private void validateCardNotActive(Transaction transaction) {
        Account account = accountRepository.findById(transaction.getIdAccount());
        if(account != null && !account.isActiveCard()){
            violations.add(MESSAGE_CARD_NOT_ACTIVE);
        }
    }

    private void validateInsufficientLimit(Transaction transaction) {
        Account account = accountRepository.findById(transaction.getIdAccount());
        if(account != null && account.getAvailableLimit() < transaction.getAmount()){
            violations.add(MESSAGE_INSUFFICIENT_LIMIT);
        }

    }

    private void validateHighFrequencySmallInterval(Transaction transaction) {
        Account account = accountRepository.findById(transaction.getIdAccount());
        if(account != null){
            LocalDateTime timeA = transaction.getTime().minusMinutes(2);
            LocalDateTime timeB =  transaction.getTime();
            List<Transaction> transactionsBefore = transactionRepository.getTransactionsInIntervalForAccount(timeA, timeB, transaction.getIdAccount());
            if(transactionsBefore.size() >= 2){
                violations.add(MESSAGE_HIGH_FREQUENCY_SMALL_INTERVAL);
                return;
            }

            timeA = transaction.getTime();
            timeB = transaction.getTime().plusMinutes(2);
            List<Transaction> transactionsAfter = transactionRepository.getTransactionsInIntervalForAccount(timeA, timeB, transaction.getIdAccount());
            if(transactionsAfter.size() >= 2){
                violations.add(MESSAGE_HIGH_FREQUENCY_SMALL_INTERVAL);
                return;
            }

            if(transactionsBefore.size() == 1 && transactionsAfter.size() == 1 && transactionsBefore.get(0).getIdTransaction() != transactionsAfter.get(0).getIdTransaction()){
                Duration duration = Duration.between(transactionsBefore.get(0).getTime(), transactionsAfter.get(0).getTime());
                if((duration.getSeconds() / 60) <= 2 ){
                    violations.add(MESSAGE_HIGH_FREQUENCY_SMALL_INTERVAL);
                }
            }
        }
    }

    private void validateDoubledTransaction(Transaction transaction) {
        Account account = accountRepository.findById(transaction.getIdAccount());
        if(account != null){
            LocalDateTime timeA = transaction.getTime().minusMinutes(2);
            LocalDateTime timeB =  transaction.getTime();
            List<Transaction> transactionsBefore = transactionRepository.getTransactionsInIntervalForAmountAndMerchant(timeA, timeB, transaction.getAmount(), transaction.getMerchant());
            if(!transactionsBefore.isEmpty()){
                violations.add(MESSAGE_DOUBLED_TRANSACTION);
                return;
            }

            timeA = transaction.getTime();
            timeB = transaction.getTime().plusMinutes(2);
            List<Transaction> transactionsAfter = transactionRepository.getTransactionsInIntervalForAmountAndMerchant(timeA, timeB, transaction.getAmount(), transaction.getMerchant());
            if(!transactionsAfter.isEmpty()){
                violations.add(MESSAGE_DOUBLED_TRANSACTION);
            }
        }
    }

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private void clearViolations(){
        this.violations = new ArrayList<>();
    }
}
