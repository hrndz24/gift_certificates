package com.epam.esm.dao;

public enum ColumnLabel {
    ID("id"),

    NAME("name"),

    TAG_NAME("tag_name"),
    TAG_ID("tag_id"),

    CERTIFICATE_NAME("name"), CERTIFICATE_DESCRIPTION("description"),
    CERTIFICATE_PRICE("price"), CERTIFICATE_CREATE_DATE("create_date"),
    CERTIFICATE_LAST_UPDATE_DATE("last_update_date"), CERTIFICATE_DURATION("duration");

    private String columnName;

    ColumnLabel(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
