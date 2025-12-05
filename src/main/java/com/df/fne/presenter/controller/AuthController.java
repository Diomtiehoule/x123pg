package com.df.fne.presenter.controller;

import com.df.fne.core.domaines.UserDto;
import com.df.fne.core.services.UserService;
import com.df.fne.presenter.request.LoginRequest;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @Hidden
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {
        Map<String,Object> map = userService.register(userDto);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/signin")
    @Hidden
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Map<String,Object> map = userService.login(loginRequest);
        return ResponseEntity.ok(map);
    }

}
