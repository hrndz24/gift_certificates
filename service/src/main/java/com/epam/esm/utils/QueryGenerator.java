package com.epam.esm.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class QueryGenerator {

    private Map<String, String> queries;
    private Map<String, String> orderByQueries;

    private static final String GET_CERTIFICATES_BY_NAME =
            "WHERE LOWER(gc.name) LIKE LOWER('?')";

    private static final String GET_CERTIFICATES_BY_DESCRIPTION =
            "WHERE LOWER(description) LIKE LOWER('?')";

    private static final String GET_CERTIFICATES_BY_TAG_NAME =
            "JOIN (SELECT certificate_tags.certificate_id FROM certificate_tags" +
                    " WHERE certificate_tags.tag_id =\n" +
                    "(SELECT id from tags WHERE LOWER(tags.name) LIKE LOWER('?'))) AS cct " +
                    "ON gc.id = cct.certificate_id";

    private static final String GET_CERTIFICATES_SORTED_BY = " ORDER BY ";

    private static final String ORDER_BY_PARAM_NAME = "orderBy";

    private static final String NAME_ASC = "gc.name";
    private static final String NAME_DESC = "gc.name DESC";
    private static final String DATE_ASC = "date";
    private static final String DATE_DESC = "date DESC";

    private StringBuilder queryBuilder;

    public QueryGenerator() {
        queries = new HashMap<>();
        orderByQueries = new HashMap<>();
        fillInQueries();
    }

    private void fillInQueries() {
        queries.put("certificateName", GET_CERTIFICATES_BY_NAME);
        queries.put("certificateDescription", GET_CERTIFICATES_BY_DESCRIPTION);
        queries.put("tagName", GET_CERTIFICATES_BY_TAG_NAME);
        queries.put("orderBy", GET_CERTIFICATES_SORTED_BY);
        orderByQueries.put("name", NAME_ASC);
        orderByQueries.put("-name", NAME_DESC);
        orderByQueries.put("date", DATE_ASC);
        orderByQueries.put("-date", DATE_DESC);
    }

    public String generateQuery(Map<String, String> params) {
        queryBuilder = new StringBuilder();
        appendQueryCondition(params);
        appendSortConditionIfExists(params);
        return queryBuilder.toString();
    }

    private void appendQueryCondition(Map<String, String> params) {
        params.keySet().forEach(s -> {
            String queryCondition = queries.get(s);
            queryCondition = queryCondition.replaceAll("\\?", "%" + params.get(s) + "%");
            queryBuilder.append(queryCondition);
        });
    }

    private void appendSortConditionIfExists(Map<String, String> params) {
        if (params.containsKey(ORDER_BY_PARAM_NAME)) {
            queryBuilder.append(orderByQueries.get(params.get(ORDER_BY_PARAM_NAME)));
        }
    }
}
