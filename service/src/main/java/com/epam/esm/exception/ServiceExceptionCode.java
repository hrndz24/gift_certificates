package com.epam.esm.exception;

public enum ServiceExceptionCode {
    CANNOT_BE_NULL("10101"), CANNOT_BE_EMPTY("10102"),
    SHOULD_BE_POSITIVE("10103"), INVALID_ORDER_BY_VALUE("10104"),
    NON_EXISTING_PARAM_NAME("10105"), CANNOT_DELETE_TAG_WHICH_IS_USED("10106"),

    NON_EXISTING_TAG_ID("20101"),
    NON_EXISTING_CERTIFICATE_ID("20102");

    private String errorCode;

    ServiceExceptionCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
