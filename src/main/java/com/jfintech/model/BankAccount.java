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

    // Getters (Để Spring Boot có thể đọc dữ liệu và chuyển sang JSON)
    public Long getId() { return id; }
    public String getOwnerName() { return ownerName; }
    public Double getBalance() { return balance; }
}