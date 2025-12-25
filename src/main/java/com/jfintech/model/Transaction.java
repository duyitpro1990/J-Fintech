package com.jfintech.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private BankAccount account;    // Giao dịch của ai?
    private Double amount;     // Số tiền (dương hoặc âm)
    private String type;       // "DEPOSIT" (Nạp) hoặc "WITHDRAW" (Rút)
    private LocalDateTime timestamp; // Thời gian thực hiện

    // Constructor rỗng (Bắt buộc cho Hibernate)
    public Transaction() {}

    // Constructor đầy đủ
    public Transaction(BankAccount account, Double amount, String type) {
        this.account = account;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now(); // Tự động lấy giờ hiện tại
    }

    // Getters
    public Long getId() { return id; }
    public BankAccount getAccount() { return account; }
    public Double getAmount() { return amount; }
    public String getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
}