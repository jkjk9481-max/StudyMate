package com.studymate.backend.user.controller;

import com.studymate.backend.user.dto.*;
import com.studymate.backend.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserResponse signUp(@RequestBody @Valid UserSignUpRequest request) {
        return userService.signUpUser(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid UserLoginRequest request) {
        return userService.loginUser(request);
    }
}
