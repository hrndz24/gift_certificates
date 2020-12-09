package com.epam.esm.exception;

public enum ServiceExceptionCode {
    CANNOT_BE_NULL("10101"), CANNOT_BE_EMPTY("10102"),
    SHOULD_BE_POSITIVE("10103"), INVALID_ORDER_BY_VALUE("10104"),
    NON_EXISTING_PARAM_NAME("10105"), CANNOT_DELETE_TAG_WHICH_IS_USED("10106"),
    CANNOT_ADD_EXISTING_TAG("10107"), DURATION_CANNOT_BE_MORE_THAN_YEAR("10108"),
    PRICE_TOO_HIGH("10109"), TAG_IS_ALREADY_ASSIGNED_TO_CERTIFICATE("10110"),
    NON_EXISTING_CERTIFICATE_FIELD_NAME("10112"),
    DATA_TYPE_DOES_NOT_MATCH_REQUIRED("10113"),
    NON_EXISTING_TAG_FIELD_NAME("10114"),
    CERTIFICATE_IS_ALREADY_ASSIGNED_TO_ORDER("10115"),
    ORDER_SHOULD_HAVE_CERTIFICATES("10116"),
    ONLY_ONE_FIELD_CAN_BE_UPDATED("10117"),
    NON_EXISTING_ORDER_FIELD("10118"),
    ORDER_SHOULD_HAVE("10119"),
    PAGE_IS_GREATER_THAN_TOTAL_AMOUNT_OF_PAGES("10120"),
    EMAIL_NOT_VALID("10121"),
    WEEK_PASSWORD("10122"),
    ACCOUNT_WITH_EMAIL_EXISTS("10123"),

    NON_EXISTING_TAG_ID("20101"),
    NON_EXISTING_CERTIFICATE_ID("20102"),
    NON_EXISTING_USER_ID("20103"),
    NON_EXISTING_ORDER_ID("20104");

    private String errorCode;

    ServiceExceptionCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
