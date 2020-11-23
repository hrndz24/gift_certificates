package com.epam.esm.dao.impl;

public enum NativeQuery {
    GET_MOST_USED_TAG("SELECT\n" +
            "    tag_id AS id, tag_name AS name\n" +
            "FROM\n" +
            "    (SELECT\n" +
            "          tag_id, tag.name AS tag_name, COUNT(tag_id) AS tag_count\n" +
            "      FROM\n" +
            "          tag\n" +
            "              JOIN certificate_has_tag ON tag.id = certificate_has_tag.tag_id\n" +
            "              JOIN gift_certificate ON certificate_has_tag.certificate_id = gift_certificate.id\n" +
            "              JOIN order_has_certificate ON gift_certificate.id = order_has_certificate.certificate_id\n" +
            "              JOIN orders ON order_has_certificate.order_id = orders.id\n" +
            "              JOIN (SELECT\n" +
            "                        SUM(cost) AS orders_cost, user_id AS ui\n" +
            "                    FROM\n" +
            "                        orders\n" +
            "                    GROUP BY user_id) AS a ON orders.user_id = a.ui\n" +
            "      WHERE\n" +
            "              orders_cost = (SELECT\n" +
            "                                 SUM(cost) AS orders_cost\n" +
            "                             FROM\n" +
            "                                 orders\n" +
            "                             GROUP BY user_id\n" +
            "                             ORDER BY orders_cost DESC\n" +
            "                             LIMIT 1)\n" +
            "      GROUP BY tag_id ORDER BY tag_count DESC LIMIT 1) AS b");

    private String query;

    NativeQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
