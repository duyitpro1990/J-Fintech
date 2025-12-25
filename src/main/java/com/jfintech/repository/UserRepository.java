package com.jfintech.repository;

import com.jfintech.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Tìm user bằng username (Ví dụ: tìm xem "TyPhu" có tồn tại không)
    Optional<User> findByUsername(String username);
}