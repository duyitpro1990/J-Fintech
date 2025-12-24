package com.jfintech.service;

import com.jfintech.model.BankAccount;
import com.jfintech.repository.AccountRepository;

import java.util.List;

import org.springframework.stereotype.Service;


@Service // Đánh dấu đây là một Bean (linh kiện) để Spring quản lý
public class AccountService {

    private final AccountRepository accountRepository;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public List<com.jfintech.model.BankAccount> getAllAccounts() {
        return accountRepository.findAll();
    }
    public BankAccount createAccount(BankAccount account) {
        return accountRepository.save(account);
    }
}