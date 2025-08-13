package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.dto.auth.RegistrationRequest;
import com.bfs.hibernateprojectdemo.dto.auth.RegistrationResponse;
import com.bfs.hibernateprojectdemo.dto.common.DataResponse;
import com.bfs.hibernateprojectdemo.service.SignupService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<DataResponse> signup(@RequestBody RegistrationRequest request) {
        User newUser = signupService.signup(request);
        RegistrationResponse response = RegistrationResponse.builder()
                .userId(newUser.getUserId())
                .email(newUser.getEmail())
                .role(newUser.getRole())
                .userName(newUser.getUsername())
                .password(newUser.getPassword())
                .build();

        DataResponse body = DataResponse.builder()
                .code(201)
                .data(response)
                .message("User registered successfully")
                .build();

        return ResponseEntity.status(201).body(body);
    }
}
