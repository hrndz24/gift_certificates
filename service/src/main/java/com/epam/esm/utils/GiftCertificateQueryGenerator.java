package com.epam.esm.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GiftCertificateQueryGenerator {

    private Map<String, String> orderByQueries;
    private Map<String, String> queries;

    private static final String ORDER_BY_PARAM = "orderBy";
    private static final String TAG_NAME_PARAM = "tagName";
    private static final String CERTIFICATE_NAME_PARAM = "certificateName";
    private static final String CERTIFICATE_DESCRIPTION_PARAM = "certificateDescription";

    private static final String GET_CERTIFICATES_BY_NAME =
            " LOWER(gc.name) LIKE LOWER('?')";

    private static final String GET_CERTIFICATES_BY_DESCRIPTION =
            " LOWER(description) LIKE LOWER('?')";

    private static final String GET_CERTIFICATES_BY_TAG_NAME =
            "gc.id IN (SELECT certificate_tag.certificate_id FROM certificate_tag" +
                    " WHERE certificate_tag.tag_id IN " +
                    "(SELECT id from tag WHERE LOWER(tag.name) LIKE LOWER('?')))";

    private static final String GET_CERTIFICATES_SORTED_BY = " ORDER BY ";
    private static final String WHERE_KEY_WORD = " WHERE ";
    private static final String AND_KEY_WORD = " AND ";
    private static final String DESCENDING_KEY_WORD = " DESC ";

    private StringBuilder queryBuilder;

    @Autowired
    public GiftCertificateQueryGenerator() {
        queries = new HashMap<>();
        orderByQueries = new HashMap<>();
        fillInQueries();
    }

    private void fillInQueries() {
        queries.put(CERTIFICATE_NAME_PARAM, GET_CERTIFICATES_BY_NAME);
        queries.put(CERTIFICATE_DESCRIPTION_PARAM, GET_CERTIFICATES_BY_DESCRIPTION);
        queries.put(TAG_NAME_PARAM, GET_CERTIFICATES_BY_TAG_NAME);
        queries.put(ORDER_BY_PARAM, GET_CERTIFICATES_SORTED_BY);
        orderByQueries.put(ServiceConstant.SORT_BY_NAME_ASC.getValue(), ServiceConstant.NAME_FIELD.getValue());
        orderByQueries.put(ServiceConstant.SORT_BY_NAME_DESC.getValue(), ServiceConstant.NAME_FIELD.getValue());
        orderByQueries.put(ServiceConstant.SORT_BY_DATE_ASC.getValue(), ServiceConstant.CREATED_DATE_FIELD.getValue());
        orderByQueries.put(ServiceConstant.SORT_BY_DATE_DESC.getValue(), ServiceConstant.CREATED_DATE_FIELD.getValue());
    }

    public String generateQueryCriteria(Map<String, String> params) {
        queryBuilder = new StringBuilder();
        buildConditions(params);
        addOrderBy(params);
        return queryBuilder.toString();
    }

    private void buildConditions(Map<String, String> params) {
        List<String> conditions = new ArrayList<>();
        params.keySet().forEach(key -> {
            switch (key) {
                case CERTIFICATE_NAME_PARAM:
                    String queryCondition = queries.get(key);
                    queryCondition = queryCondition.replaceAll("\\?", "%" + params.get(key) + "%");
                    conditions.add(queryCondition);
                    break;
                case CERTIFICATE_DESCRIPTION_PARAM:
                    queryCondition = queries.get(key);
                    queryCondition = queryCondition.replaceAll("\\?", "%" + params.get(key) + "%");
                    conditions.add(queryCondition);
                    break;
                case TAG_NAME_PARAM:
                    conditions.add(addTagNamePredicate(params.get(key)));
                    break;
                default:
                    break;
            }
        });
        appendsConditions(conditions);
    }

    private void appendsConditions(List<String> conditions) {
        if (!conditions.isEmpty()) {
            queryBuilder.append(WHERE_KEY_WORD);
            conditions.forEach(condition -> {
                queryBuilder.append(condition).append(AND_KEY_WORD);
            });
            queryBuilder.delete(queryBuilder.length() - AND_KEY_WORD.length(), queryBuilder.length() - 1);
        }
    }


    private String addTagNamePredicate(String tagNamesAsString) {
        String[] tagNames = tagNamesAsString.split(ServiceConstant.TAGS_TO_SEARCH_BY_SEPARATOR.getValue());
        StringBuilder query = new StringBuilder("");
        for (String tagName : tagNames) {
            String tagNameSearch = GET_CERTIFICATES_BY_TAG_NAME.replaceAll("\\?", "%" + tagName + "%");
            query.append(tagNameSearch).append(AND_KEY_WORD);
        }
        return query.substring(0, query.length() - AND_KEY_WORD.length());
    }

    private void addOrderBy(Map<String, String> params) {
        if (params.containsKey(ORDER_BY_PARAM)) {
            String value = params.get(ORDER_BY_PARAM);
            queryBuilder.append(GET_CERTIFICATES_SORTED_BY).append(orderByQueries.get(value));
            if (value.startsWith("-")) {
                queryBuilder.append(DESCENDING_KEY_WORD);
            }
        }
    }
}
