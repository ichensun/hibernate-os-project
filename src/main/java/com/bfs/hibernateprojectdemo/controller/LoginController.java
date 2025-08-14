package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.dto.auth.LoginRequest;
import com.bfs.hibernateprojectdemo.dto.auth.LoginResponse;
import com.bfs.hibernateprojectdemo.security.AuthUserDetail;
import com.bfs.hibernateprojectdemo.security.JwtProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    private AuthenticationManager authenticationManager;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private JwtProvider jwtProvider;

    @Autowired
    private void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        Authentication authentication;

        // authenticate the user using the username and password
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Provided credential is invalid.");
        }

        AuthUserDetail authUserDetail = (AuthUserDetail) authentication.getPrincipal();

        String token = jwtProvider.createToken(authUserDetail);
        System.out.println(token);

        return LoginResponse.builder()
                .message("Welcome: " + authUserDetail.getUsername())
                .token(token)
                .build();
    }

}
