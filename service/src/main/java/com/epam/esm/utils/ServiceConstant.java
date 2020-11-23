package com.epam.esm.utils;

public enum ServiceConstant {

    ORDER_BY_PARAM("orderBy"),
    TAG_NAME_PARAM("tagName"),
    CERTIFICATE_NAME_PARAM("certificateName"),
    CERTIFICATE_DESCRIPTION_PARAM("certificateDescription"),

    USER_ID_PARAM("userId"),

    PAGE_PARAM("page"), SIZE_PARAM("size"),
    ID_FIELD("id"), NAME_FIELD("name"),
    DESCRIPTION_FIELD("description"),
    TAGS_FIELD("tags"), CREATED_DATE_FIELD("createDate"),
    DURATION_FIELD("duration"), PRICE_FIELD("price"),

    SORT_BY_NAME_ASC("name"),
    SORT_BY_NAME_DESC("-name"),
    SORT_BY_DATE_ASC("date"),
    SORT_BY_DATE_DESC("-date"),

    TAGS_TO_SEARCH_BY_SEPARATOR(", ");

    private final String value;

    ServiceConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
