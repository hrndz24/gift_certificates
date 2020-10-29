package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ValidatorException.class)
    public ResponseEntity<ExceptionResponse> handleValidatorException(ValidatorException e) {
        ExceptionResponse response = new ExceptionResponse(40001, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DAOException.class)
    public ResponseEntity<ExceptionResponse> handleDAOException(DAOException e) {
        ExceptionResponse response = new ExceptionResponse(40002, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
