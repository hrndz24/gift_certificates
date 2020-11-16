package com.epam.esm.dao;

public enum ColumnLabel {
    ID("id"),

    NAME("name"),

    TAG_NAME("tag_name"),
    TAG_ID("tag_id"),

    CERTIFICATE_NAME("name"), CERTIFICATE_DESCRIPTION("description"),
    CERTIFICATE_PRICE("price"), CERTIFICATE_CREATE_DATE("create_date"),
    CERTIFICATE_LAST_UPDATE_DATE("last_update_date"), CERTIFICATE_DURATION("duration"),

    USER_EMAIL("email"), USER_PASSWORD("password"),

    ORDER_ID("order_id"), USER_ID("user_id"),
    ORDER_COST("cost"), ORDER_DATE("date");

    private String columnName;

    ColumnLabel(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
