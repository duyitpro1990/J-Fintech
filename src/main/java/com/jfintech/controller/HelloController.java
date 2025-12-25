package com.jfintech.controller; 

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.jfintech.model.BankAccount;
import com.jfintech.model.Transaction;
import com.jfintech.service.AccountService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class HelloController {
    // Khai báo Service
    private final AccountService accountService;

    public HelloController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Chào mừng bạn đến với kiến trúc MVC của Spring Boot!";
    }
    
    // Tạo thêm một API nữa để test
    @GetMapping("/admin")
    public String adminPage() {
        return "Đây là trang Admin (Chưa bảo mật)";
    }

    // API trả về JSON
    @GetMapping("/my-account")
    public BankAccount getMyAccount() {
        // Giả lập dữ liệu từ Database
        BankAccount account = new BankAccount(1001L, "Nguyen Van A", 5000000.0);
        
        return account; // Trả về Object, không phải String
    }
    // API trả về danh sách tài khoản
    @GetMapping("/accounts")
    public List<BankAccount> getAccounts() {
        // Lễ tân gọi xuống bếp lấy đồ ăn
        return accountService.getAllAccounts();
    }
    // API tạo tài khoản mới
    // Khách hàng gửi JSON -> Server nhận và lưu
    @PostMapping("/accounts")
    public BankAccount createAccount(@RequestBody BankAccount newAccount) {
        accountService.createAccount(newAccount);
        return newAccount; // Trả lại chính tài khoản vừa tạo để xác nhận
    }

    // API nạp tiền vào tài khoản
    // Cách gọi: PUT http://localhost:8080/accounts/1/deposit?amount=50000
    @PutMapping("/accounts/{accountId}/deposit")
    public BankAccount deposit(@PathVariable Long accountId, @RequestParam Double amount) {
        return accountService.deposit(accountId, amount);
    }

    // API rút tiền khỏi tài khoản
    // Cách gọi: PUT http://localhost:8080/accounts/1/withdraw?amount=20000
    @PutMapping("/accounts/{accountId}/withdraw")
    public BankAccount withdraw(@PathVariable Long accountId, @RequestParam Double amount) {
        return accountService.withdraw(accountId, amount);
    }
    // API get account by ID
    @GetMapping("/accounts/{accountId}")
    public BankAccount getAccountById(@PathVariable Long accountId) {
        return accountService.getAccountById(accountId);
    }

    //API xem lịch sử giao dịch
    @GetMapping("/accounts/{accountId}/transactions")
    public List<Transaction> getTransactionsForAccount(@PathVariable Long accountId) {
        return accountService.getTransactionsForAccount(accountId);
    }
}