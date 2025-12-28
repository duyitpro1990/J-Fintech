package com.jfintech.service;

import com.jfintech.model.User;
import com.jfintech.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AccountService accountService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
    }

    @Transactional
    public User registerUser(String username, String password) {
        // Kiểm tra trùng username (Best practice)
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        // A. Tạo User
        String encodedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, encodedPassword, "ROLE_USER");
        User savedUser = userRepository.save(newUser);
        // B. Tự động tạo BankAccount kèm theo
        accountService.createBankAccountForUser(savedUser);
        return savedUser;
    }
}