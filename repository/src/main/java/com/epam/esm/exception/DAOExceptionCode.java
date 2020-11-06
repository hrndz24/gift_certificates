package com.epam.esm.exception;

public enum DAOExceptionCode {
    FAILED_ADD_TAG("30101"), FAILED_REMOVE_TAG("30102"),
    FAILED_GET_TAG("30103"), FAILED_GET_TAGS("30104"),

    FAILED_ADD_CERTIFICATE("40101"), FAILED_REMOVE_CERTIFICATE("40102"),
    FAILED_UPDATE_CERTIFICATE("40103"), FAILED_GET_CERTIFICATE("40104"),
    FAILED_GET_CERTIFICATES("40105"), FAILED_ADD_TAG_TO_CERTIFICATE("40106"),
    FAILED_REMOVE_TAG_FROM_CERTIFICATE("40107"), ERROR_DURING_FINDING_ASSIGNED_TAGS("40108");

    private String errorCode;

    DAOExceptionCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
