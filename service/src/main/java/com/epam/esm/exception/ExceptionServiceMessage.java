package com.epam.esm.exception;

public enum ExceptionServiceMessage {
    NULL("10101"), EMPTY("10102"),
    NEGATIVE("10103"), INVALID_ORDER_BY_VALUE("10104"),
    NON_EXISTING_PARAM_NAME("10105"),

    NON_EXISTING_TAG_ID("20101"),
    NON_EXISTING_CERTIFICATE_ID("20102");

    private String errorCode;

    ExceptionServiceMessage(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
