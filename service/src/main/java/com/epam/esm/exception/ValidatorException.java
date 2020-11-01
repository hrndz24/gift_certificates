package com.epam.esm.exception;

public class ValidatorException extends RuntimeException {

    private String parameter;

    public ValidatorException() {
    }

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String message, String parameter) {
        super(message);
        this.parameter = parameter;
    }

    public ValidatorException(String message, Throwable cause, String parameter) {
        super(message, cause);
        this.parameter = parameter;
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }

    public ValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getParameter() {
        return parameter;
    }
}