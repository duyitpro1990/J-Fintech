package com.jfintech.controller;

import com.jfintech.model.User;
import com.jfintech.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // API Đăng ký: POST http://localhost:8080/register
    @PostMapping("/register")
    public User register(@RequestParam String username, @RequestParam String password) {
        return userService.registerUser(username, password);
    }
}