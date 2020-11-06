package com.epam.esm.exception;

public class EntityNotFoundException extends RuntimeException {

    private String id;

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message, String id) {
        super(message);
        this.id = id;
    }

    public EntityNotFoundException(String message, Throwable cause, String id) {
        super(message, cause);
        this.id = id;
    }

    public EntityNotFoundException(Throwable cause, String id) {
        super(cause);
        this.id = id;
    }

    public EntityNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getId() {
        return id;
    }
}
