package com.epam.esm.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GiftCertificateQueryGenerator {

    private Map<String, String> queries;
    private Map<String, String> orderByQueries;

    private static final String GET_CERTIFICATES_BY_NAME =
            "WHERE LOWER(name) LIKE LOWER('%?%')";

    private static final String GET_CERTIFICATES_BY_DESCRIPTION =
            "WHERE LOWER(description) LIKE LOWER('%?%')";

    private static final String GET_CERTIFICATES_BY_TAG_NAME = " WHERE Tag.name LIKE LOWER('%?%')";
    /*"id IN (SELECT certificate_has_tag.certificate_id FROM certificate_has_tag" +
            " WHERE certificate_has_tag.tag_id IN\n" +
            "(SELECT id from tag WHERE LOWER(tag.name) LIKE LOWER('%?%')))";
*/
    private static final String ORDER_BY = " ORDER BY ";

    private static final String ORDER_BY_PARAM_NAME = "orderBy";
    private static final String TAG_NAME_PARAM_NAME = "tagName";

    private static final String NAME_ASC = "name";
    private static final String NAME_DESC = "name DESC";
    private static final String DATE_ASC = "createDate";
    private static final String DATE_DESC = "createDate DESC";

    private StringBuilder queryBuilder;

    public GiftCertificateQueryGenerator() {
        queries = new HashMap<>();
        orderByQueries = new HashMap<>();
        fillInQueries();
    }

    private void fillInQueries() {
        queries.put("certificateName", GET_CERTIFICATES_BY_NAME);
        queries.put("certificateDescription", GET_CERTIFICATES_BY_DESCRIPTION);
        queries.put("tagName", GET_CERTIFICATES_BY_TAG_NAME);
        queries.put("orderBy", ORDER_BY);
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
            if (!ORDER_BY_PARAM_NAME.equals(s)) {
                String queryCondition = queries.get(s);
                if (TAG_NAME_PARAM_NAME.equals(s)) {
                    queryCondition = buildSearchByTagsQuery(params.get(s));
                } else {
                    queryCondition = queryCondition.replaceAll("\\?", params.get(s));
                }
                queryBuilder.append(queryCondition);
            }
        });
    }

    private String buildSearchByTagsQuery(String tagNamesAsString) {
        String[] tagNames = tagNamesAsString.split(", ");
        StringBuilder query = new StringBuilder();
        for (String tagName : tagNames) {
            String tagNameSearch = GET_CERTIFICATES_BY_TAG_NAME.replaceAll("\\?", tagName);
            query.append(tagNameSearch).append(" AND ");
        }
        return query.substring(0, query.length() - " AND ".length());
    }

    private void appendSortConditionIfExists(Map<String, String> params) {
        if (params.containsKey(ORDER_BY_PARAM_NAME)) {
            queryBuilder.append(queries.get(ORDER_BY_PARAM_NAME));
            queryBuilder.append(orderByQueries.get(params.get(ORDER_BY_PARAM_NAME)));
        }
    }
}
