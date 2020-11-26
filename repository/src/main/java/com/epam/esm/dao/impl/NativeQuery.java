package com.epam.esm.dao.impl;

public enum NativeQuery {
    GET_MOST_USED_TAG(Constant.GET_MOST_USED_TAG_QUERY),
    GET_CERTIFICATES(Constant.GET_CERTIFICATES),
    GET_ORDERS(Constant.GET_ORDERS);

    private String query;

    NativeQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    private static class Constant {

        private static StringBuilder stringBuilder = new StringBuilder();
        private static final String GET_MOST_USED_TAG_QUERY = stringBuilder.append("SELECT ")
                .append("    tag_id AS id, tag_name AS name ")
                .append("FROM ")
                .append("    (SELECT ")
                .append("          tag_id, tag.name AS tag_name, COUNT(tag_id) AS tag_count ")
                .append("      FROM ")
                .append("          tag ")
                .append("              JOIN certificate_tag ON tag.id = certificate_tag.tag_id ")
                .append("              JOIN gift_certificate ON certificate_tag.certificate_id = gift_certificate.id ")
                .append("              JOIN order_certificate ON gift_certificate.id = order_certificate.certificate_id ")
                .append("              JOIN orders ON order_certificate.order_id = orders.id ")
                .append("              JOIN (SELECT ")
                .append("                        SUM(cost) AS orders_cost, user_id AS ui ")
                .append("                    FROM ")
                .append("                        orders ")
                .append("                    GROUP BY user_id) AS a ON orders.user_id = a.ui ")
                .append("      WHERE ")
                .append("              orders_cost = (SELECT ")
                .append("                                 SUM(cost) AS orders_cost ")
                .append("                             FROM ")
                .append("                                 orders ")
                .append("                             GROUP BY user_id ")
                .append("                             ORDER BY orders_cost DESC ")
                .append("                             LIMIT 1) ")
                .append("      GROUP BY tag_id ORDER BY tag_count DESC LIMIT 1) AS b").toString();

        private static final String ALL_CERTIFICATE_FIELDS = "gc.id certificate_id, gc.name name, description," +
                " price, create_date, last_update_date, duration, tag.id as tag_id, tag.name as tag_name";
        private static final String JOIN_TAGS = " LEFT JOIN certificate_tag ct " +
                "ON gc.id = ct.certificate_id LEFT JOIN tag ON ct.tag_id = tag.id ";
        private static final String GET_CERTIFICATES = "SELECT " + ALL_CERTIFICATE_FIELDS +
                " FROM gift_certificate gc " + JOIN_TAGS;
        private static final String ALL_ORDER_FIELDS = "o.id as id, user_id, cost, o.date";
        private static final String JOIN_CERTIFICATES = " LEFT JOIN order_certificate ohc ON o.id=ohc.order_id " +
                "LEFT JOIN gift_certificate gc ON gc.id = ohc.certificate_id ";
        private static final String GET_ORDERS = "SELECT " + ALL_ORDER_FIELDS + ", " + ALL_CERTIFICATE_FIELDS +
                " FROM orders o" + JOIN_CERTIFICATES + JOIN_TAGS;
    }
}
