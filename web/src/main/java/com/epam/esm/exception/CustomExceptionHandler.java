package com.epam.esm.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class CustomExceptionHandler {

    private MessageSource messageSource;

    @Autowired
    public CustomExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ValidatorException.class)
    public ResponseEntity<ExceptionResponse> handleValidatorException(ValidatorException e, Locale locale) {
        String errorMessage = messageSource.getMessage(
                "error.validation", new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(400, errorMessage + " " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DAOException.class)
    public ResponseEntity<ExceptionResponse> handleDAOException(DAOException e, Locale locale) {
        String errorMessage = messageSource.getMessage(
                "error.not_found", new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(500, errorMessage + " " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e, Locale locale) {
        String errorMessage = messageSource.getMessage(
                "error.not_found", new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(404, errorMessage + " " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
