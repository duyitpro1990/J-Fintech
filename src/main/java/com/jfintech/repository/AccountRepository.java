package com.jfintech.repository;

import com.jfintech.model.BankAccount;
import com.jfintech.model.User;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
// JpaRepository<Kiểu dữ liệu, Kiểu của ID>
public interface AccountRepository extends JpaRepository<BankAccount, Long> {
    // Không cần viết gì thêm, nó đã có sẵn hàm findAll(), save(), findById()...
    // Tìm danh sách tài khoản theo số tài khoản
    List<BankAccount> findByUser(User user);
    long countByCreatedAt(LocalDate date);
    boolean existsByAccountNumber(String accountNumber);
    // Tìm tài khoản theo số tài khoản
    Optional<BankAccount> findByAccountNumber(String accountNumber);
}