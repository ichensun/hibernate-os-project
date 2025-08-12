package com.bfs.hibernateprojectdemo.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UsernameFoundException extends RuntimeException {

    public UsernameFoundException(String message) {
        super(message);
    }
}
