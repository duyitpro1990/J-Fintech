package com.jfintech.service;

import com.jfintech.model.BankAccount;
import com.jfintech.model.Transaction;
import com.jfintech.model.User;
import com.jfintech.repository.AccountRepository;
import com.jfintech.repository.TransactionRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public List<BankAccount> getAccountsByUser(User user) {
        return accountRepository.findByUser(user);
    }
    // --- HÀM MỚI: TẠO TÀI KHOẢN CHO USER ---
    // Hàm này sẽ được UserService gọi ngay khi đăng ký thành công
    public void createBankAccountForUser(User user) {
        BankAccount newAccount = new BankAccount();
        newAccount.setUser(user);
        newAccount.setOwnerName(user.getUsername());
        newAccount.setBalance(0.0); // Mặc định 0đ
        newAccount.setCreatedAt(LocalDate.now());

        // LOGIC SINH SỐ TÀI KHOẢN (ddMMyyXXX)
        String accountNumber = generateUniqueAccountNumber();
        newAccount.setAccountNumber(accountNumber);

        accountRepository.save(newAccount);
    }

    // Hàm phụ trợ: Sinh số không trùng (Logic while loop)
    private String generateUniqueAccountNumber() {
        LocalDate today = LocalDate.now();
        String datePart = today.format(DateTimeFormatter.ofPattern("ddMMyy"));
        
        long countToday = accountRepository.countByCreatedAt(today);
        long sequence = countToday + 1;
        String candidate = "";

        do {
            String seqPart = String.format("%03d", sequence);
            candidate = datePart + seqPart;
            sequence++; 
        } while (accountRepository.existsByAccountNumber(candidate));

        return candidate;
    }
    public BankAccount createAccount(BankAccount newAccount) {
        if(newAccount.getCreatedAt() == null) newAccount.setCreatedAt(LocalDate.now());
         newAccount.setAccountNumber(generateUniqueAccountNumber());
         return accountRepository.save(newAccount);
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

    //transferMoney method
    public void transferMoney(Long fromAccountId, String toAccountNumber, Double amount) {
        BankAccount fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("From account not found")); 
        
        //Tìm tài khoản nhận    
        BankAccount toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("To account not found"));

        // --- FIX BUG: CHẶN CHUYỂN CHO CHÍNH MÌNH ---
        if (fromAccount.getId().equals(toAccount.getId())) {
            throw new RuntimeException("Không thể chuyển tiền cho chính tài khoản nguồn!");
        }
        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        // Lưu lịch sử giao dịch
        Transaction tx1 = new Transaction(fromAccount, -amount, "TRANSFER_OUT (to " + toAccountNumber + ")");
        Transaction tx2 = new Transaction(toAccount, amount, "TRANSFER_IN (from " + fromAccount.getAccountNumber() + ")");
        transactionRepository.save(tx1);
        transactionRepository.save(tx2);
    }
}