package com.epam.esm.dao.impl;

public enum SQLQuery {
    DELETE_TAG("DELETE FROM tag WHERE id = ?"),
    GET_ALL_TAGS("SELECT id as tag_id, name as tag_name FROM tag ORDER BY id"),
    GET_TAG_BY_ID("SELECT id as tag_id, name as tag_name FROM tag WHERE id = ?"),

    DELETE_GIFT_CERTIFICATE("DELETE FROM gift_certificate WHERE id = ?"),
    UPDATE_GIFT_CERTIFICATE("UPDATE gift_certificate SET name = ?, description = ?, price = ?," +
            " last_update_date = ?, duration = ? WHERE id = ?"),
    ALL_CERTIFICATE_FIELDS("gc.id id, gc.name name, description, price, create_date, " +
            "last_update_date, duration, tag_id, tag.name as tag_name"),
    JOIN_TAGS(" LEFT JOIN certificate_has_tag ct " +
            "ON gc.id = ct.certificate_id LEFT JOIN tag ON ct.tag_id = tag.id "),
    GET_CERTIFICATES("SELECT " + ALL_CERTIFICATE_FIELDS.query + " FROM gift_certificate gc " + JOIN_TAGS.query),
    GET_CERTIFICATE_BY_ID(GET_CERTIFICATES.query + " WHERE gc.id = ?"),

    ADD_TAG_TO_CERTIFICATE("INSERT INTO certificate_has_tag(certificate_id, tag_id) VALUES (?, ?)"),
    REMOVE_TAG_FROM_CERTIFICATE("DELETE FROM certificate_has_tag WHERE certificate_id = ? AND tag_id = ?"),
    REMOVE_ALL_TAGS_FROM_CERTIFICATE("DELETE FROM certificate_has_tag WHERE certificate_id = ?"),
    IS_TAG_ASSIGNED("SELECT COUNT(*) FROM certificate_has_tag WHERE tag_id = ?"),
    IS_TAG_ASSIGNED_TO_CERTIFICATE("SELECT COUNT(*) FROM certificate_has_tag WHERE tag_id = ? AND certificate_id = ?"),

    GET_ALL_USERS("SELECT id, email, password FROM user ORDER BY id"),
    GET_USER_BY_ID("SELECT id, email, password FROM user WHERE id = ?"),

    ALL_ORDER_FIELDS("o.id as order_id, user_id, cost, o.date"),
    JOIN_CERTIFICATES(" LEFT JOIN order_has_certificate ohc ON o.id=ohc.order_id \n" +
            "LEFT JOIN gift_certificate gc ON gc.id = ohc.certificate_id "),
    GET_ORDERS("SELECT " + ALL_ORDER_FIELDS.query + ", " + ALL_CERTIFICATE_FIELDS.query +
            " FROM `order` o" + JOIN_CERTIFICATES.query + JOIN_TAGS.query),
    GET_ORDER_BY_ID(GET_ORDERS.query + " WHERE order_id = ?"),
    GET_ORDERS_BY_USER_ID(GET_ORDERS.query + "WHERE user_id = ?"),

    ADD_CERTIFICATE_TO_ORDER("INSERT INTO order_has_certificate(order_id, certificate_id) VALUES(?, ?)"),
    IS_CERTIFICATE_ASSIGNED_TO_ORDER("SELECT COUNT(*) FROM order_has_certificate WHERE order_id = ? AND certificate_id = ?");

    private String query;

    SQLQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
