package com.epam.esm.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
        String localizedMessage = messageSource.getMessage(
                e.getMessage(), new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(e.getMessage(),
                buildErrorMessage(localizedMessage, e.getParameter()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e, Locale locale) {
        String localizedMessage = messageSource.getMessage(
                e.getMessage(), new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), buildErrorMessage(localizedMessage, e.getId()));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DAOException.class)
    public ResponseEntity<ExceptionResponse> handleDAOException(DAOException e, Locale locale) {
        String localizedMessage = messageSource.getMessage(
                e.getMessage(), new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), localizedMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleMediaTypeException(HttpMediaTypeNotSupportedException e, Locale locale) {
        String errorMessage = messageSource.getMessage(
                "50101", new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse("50101", errorMessage);
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(HttpClientErrorException.BadRequest e, Locale locale) {
        String errorMessage = messageSource.getMessage(
                "50102", new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse("50102", errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, Locale locale) {
        String errorMessage = messageSource.getMessage(
                "50103", new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse("50103", errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, Locale locale) {
        String errorMessage = messageSource.getMessage(
                "50104", new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse("50104", errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, Locale locale) {
        String errorMessage = messageSource.getMessage(
                "50105", new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse("50105", errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException e, Locale locale) {
        String errorMessage = messageSource.getMessage(
                "50106", new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse("50106", errorMessage);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    private String buildErrorMessage(String localizedMessage, String parameter) {
        return parameter == null ? localizedMessage : localizedMessage + " " + parameter;
    }
}
