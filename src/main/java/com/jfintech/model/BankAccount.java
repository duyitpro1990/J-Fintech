package com.jfintech.model;
import jakarta.persistence.*;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bank_accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ownerName; // Tên chủ tài khoản
    private Double balance;   // Số dư

    @Column(unique = true)
    private String accountNumber;
    private LocalDate createdAt;
    public String getAccountNumber() {
        return accountNumber;
    }   
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    
    @ManyToOne
    @JoinColumn(name = "user_id") // Tạo cột user_id trong bảng bank_accounts
    @JsonIgnore // Khi in BankAccount, đừng in ngược lại User (để giấu pass và tránh lặp)
    private User user;

    // Getter & Setter cho User
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    // Constructor (Hàm khởi tạo)
    public BankAccount(Long id, String ownerName, Double balance) {
        this.id = id;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    // Default constructor (Hàm khởi tạo mặc định)
    public BankAccount() {
        this.balance = 0.0;
    }

    // Getters (Để Spring Boot có thể đọc dữ liệu và chuyển sang JSON)
    public Long getId() { return id; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public Double getBalance() { return balance; }
    //set balance
    public void setBalance(Double balance) { this.balance = balance; }

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Transaction> transactions;
    public java.util.List<Transaction> getTransactions() {
        return transactions;
    }
    public void setTransactions(java.util.List<Transaction> transactions) {
        this.transactions = transactions;
    }
}