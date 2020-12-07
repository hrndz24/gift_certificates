package com.epam.esm.dao.impl;

public enum DAOConstant {
    GET_MOST_USED_TAG_QUERY(Constant.GET_MOST_USED_TAG_QUERY),

    ID_FIELD(Constant.ID_FIELD),
    NAME_FIELD(Constant.NAME_FIELD),

    USER_ID_FIELD(Constant.USER_ID_FIELD),
    TAGS_FIELD(Constant.TAGS_FIELD),
    DESCRIPTION_FIELD(Constant.DESCRIPTION_FIELD);

    private String value;

    DAOConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
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

        private static final String ID_FIELD = "id";
        private static final String NAME_FIELD = "name";
        private static final String USER_ID_FIELD = "userId";
        private static final String TAGS_FIELD = "tags";
        private static final String DESCRIPTION_FIELD = "description";
    }
}
