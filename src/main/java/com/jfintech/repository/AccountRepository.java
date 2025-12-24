package com.jfintech.repository;

import com.jfintech.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// JpaRepository<Kiểu dữ liệu, Kiểu của ID>
public interface AccountRepository extends JpaRepository<BankAccount, Long> {
    // Không cần viết gì thêm, nó đã có sẵn hàm findAll(), save(), findById()...
}