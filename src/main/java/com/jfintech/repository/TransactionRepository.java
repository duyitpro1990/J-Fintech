package com.jfintech.repository;

import com.jfintech.model.BankAccount;
import com.jfintech.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountOrderByTimestampDesc(BankAccount account);
    // Hoặc Cách 2 (vẫn dùng ID): Tìm theo thuộc tính id của đối tượng account
    // Cú pháp: findBy + TênBiếnCha + _ + TênThuộcTínhCon
    // List<Transaction> findByAccount_IdOrderByTimestampDesc(Long accountId);
}