package com.epam.esm.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderQueryGenerator {

    private Map<String, String> queries;

    private static final String GET_BY_USER_ID = " WHERE user_id = ?";

    private StringBuilder queryBuilder;

    public OrderQueryGenerator() {
        queries = new HashMap<>();
        fillInQueries();
    }

    private void fillInQueries() {
        queries.put("userId", GET_BY_USER_ID);
    }

    public String generateQuery(Map<String, String> params) {
        queryBuilder = new StringBuilder();
        appendQueryCondition(params);
        return queryBuilder.toString();
    }

    private void appendQueryCondition(Map<String, String> params) {
        params.keySet().forEach(s -> {
            String queryCondition = queries.get(s);
            queryCondition = queryCondition.replaceAll("\\?", params.get(s));
            queryBuilder.append(queryCondition);
        });
    }
}
