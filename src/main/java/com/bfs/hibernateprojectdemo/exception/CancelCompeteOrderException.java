package com.bfs.hibernateprojectdemo.exception;

public class CancelCompeteOrderException extends RuntimeException {
    public CancelCompeteOrderException(String message) {
        super(message);
    }
}
