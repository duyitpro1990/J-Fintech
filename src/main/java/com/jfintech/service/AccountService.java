package com.jfintech.service;

import com.jfintech.model.BankAccount;
import com.jfintech.model.Transaction;
import com.jfintech.repository.AccountRepository;
import com.jfintech.repository.TransactionRepository;

import java.util.List;

import org.springframework.stereotype.Service;


@Service // Đánh dấu đây là một Bean (linh kiện) để Spring quản lý
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }
    public List<BankAccount> getAllAccounts() {
        return accountRepository.findAll();
    }
    public BankAccount createAccount(BankAccount account) {
        return accountRepository.save(account);
    }
    //Deposit method
    public BankAccount deposit(Long accountId, Double amount) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        // Lưu lịch sử giao dịch
        Transaction tx = new Transaction(account, amount, "DEPOSIT");
        transactionRepository.save(tx);
        return account;
    }
    //Withdraw method
    public BankAccount withdraw(Long accountId, Double amount) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        // Lưu lịch sử giao dịch
        Transaction transaction = new Transaction(account, -amount, "WITHDRAW");
        transactionRepository.save(transaction);
        return account;
    }

    //Get account by ID method
    public BankAccount getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
    //Get all transactions for an account
    public List<Transaction> getTransactionsForAccount(Long accountId) {
        BankAccount account = getAccountById(accountId);
        if (account == null) return List.of();
        return transactionRepository.findByAccountOrderByTimestampDesc(account);
    }
}