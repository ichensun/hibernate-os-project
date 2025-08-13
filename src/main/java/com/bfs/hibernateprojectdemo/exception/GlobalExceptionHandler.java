package com.bfs.hibernateprojectdemo.exception;

import com.bfs.hibernateprojectdemo.dto.common.DataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // add your own exception handlers
    @ExceptionHandler(TooManyQuestionsException.class)
    public ResponseEntity<DataResponse> handleTooManyQuestionsExceptions(TooManyQuestionsException ex) {
        DataResponse body = DataResponse.builder().message(ex.getMessage()).build();
        return ResponseEntity.status(400).body(body);
    }

    @ExceptionHandler(UsernameFoundException.class)
    public ResponseEntity<DataResponse> handleUsernameFoundException(UsernameFoundException exception) {
        DataResponse body = DataResponse.builder()
                .code(409)
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(409).body(body);
    }

    @ExceptionHandler(NotEnoughInventoryException.class)
    public ResponseEntity<String> handleInventoryException(NotEnoughInventoryException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(CancelCompeteOrderException.class)
    public ResponseEntity<String> handleCancelCompleteOrderException(CancelCompeteOrderException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
