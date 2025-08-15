package com.bfs.hibernateprojectdemo.service;

import com.bfs.hibernateprojectdemo.dao.SignupDao;
import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.dto.auth.RegistrationRequest;
import com.bfs.hibernateprojectdemo.exception.UsernameFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class SignupService {

    private final SignupDao signupDao;
    private final PasswordEncoder passwordEncoder;

    public User signup(RegistrationRequest request) {

        String userName = request.getUserName();
        String email = request.getEmail();
        String password = request.getPassword();

        Optional<User> isUsernameExist = signupDao.findUserByUsername(userName);
        if (isUsernameExist.isPresent()) {
            throw new UsernameFoundException("Username already exists, please" +
                    " use a different username");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User savedUser = signupDao.createUser(userName, email, encodedPassword);

        return User.builder()
                .userId(savedUser.getUserId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .password(savedUser.getPassword())
                .build();
    }
}
