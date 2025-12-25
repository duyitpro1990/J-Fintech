package com.jfintech.model;
import jakarta.persistence.*;

@Entity
@Table(name = "bank_accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ownerName; // Tên chủ tài khoản
    private Double balance;   // Số dư

    // Constructor (Hàm khởi tạo)
    public BankAccount(Long id, String ownerName, Double balance) {
        this.id = id;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    // Default constructor (Hàm khởi tạo mặc định)
    public BankAccount() {}

    // Getters (Để Spring Boot có thể đọc dữ liệu và chuyển sang JSON)
    public Long getId() { return id; }
    public String getOwnerName() { return ownerName; }
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