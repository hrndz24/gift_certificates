package com.epam.esm.dao.impl;

public enum NativeQuery {
    GET_MOST_USED_TAG(Constant.GET_MOST_USED_TAG_QUERY);

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
                .append("              JOIN certificate_has_tag ON tag.id = certificate_has_tag.tag_id ")
                .append("              JOIN gift_certificate ON certificate_has_tag.certificate_id = gift_certificate.id ")
                .append("              JOIN order_has_certificate ON gift_certificate.id = order_has_certificate.certificate_id ")
                .append("              JOIN orders ON order_has_certificate.order_id = orders.id ")
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
    }
}
