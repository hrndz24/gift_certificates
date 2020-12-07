package com.epam.esm.utils;

import com.epam.esm.specification.Specification;
import com.epam.esm.specification.impl.certificate.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GiftCertificateQueryGenerator {

    private Map<String, String> orderByQueries;
    private static final String ORDER_BY_PARAM = "orderBy";
    private static final String TAG_NAME_PARAM = "tagName";
    private static final String CERTIFICATE_NAME_PARAM = "certificateName";
    private static final String CERTIFICATE_DESCRIPTION_PARAM = "certificateDescription";

    private List<Specification> specifications;

    public GiftCertificateQueryGenerator() {
        this.orderByQueries = new HashMap<>();
        fillInQueries();
    }

    private void fillInQueries() {
        orderByQueries.put(ServiceConstant.SORT_BY_NAME_ASC.getValue(), ServiceConstant.NAME_FIELD.getValue());
        orderByQueries.put(ServiceConstant.SORT_BY_NAME_DESC.getValue(), ServiceConstant.NAME_FIELD.getValue());
        orderByQueries.put(ServiceConstant.SORT_BY_DATE_ASC.getValue(), ServiceConstant.CREATED_DATE_FIELD.getValue());
        orderByQueries.put(ServiceConstant.SORT_BY_DATE_DESC.getValue(), ServiceConstant.CREATED_DATE_FIELD.getValue());
    }

    public List<Specification> generateQueryCriteria(Map<String, String> params) {
        specifications = new ArrayList<>();
        buildConditions(params);
        return specifications;
    }

    private void buildConditions(Map<String, String> params) {
        params.keySet().forEach(key -> {
            switch (key) {
                case CERTIFICATE_NAME_PARAM:
                    specifications.add(new GetCertificatesByName(params.get(key)));
                    break;
                case CERTIFICATE_DESCRIPTION_PARAM:
                    specifications.add(new GetCertificatesByDescription(params.get(key)));
                    break;
                case TAG_NAME_PARAM:
                    addTagNamePredicate(params.get(key));
                    break;
                case ORDER_BY_PARAM:
                    addOrderBy(params);
                default:
                    break;
            }
        });
    }

    private void addTagNamePredicate(String tagNamesAsString) {
        String[] tagNames = tagNamesAsString.split(ServiceConstant.TAGS_TO_SEARCH_BY_SEPARATOR.getValue());
        for (String tagName : tagNames) {
            specifications.add(new GetCertificatesByTagName(tagName));
        }
    }

    private void addOrderBy(Map<String, String> params) {
        String value = params.get(ORDER_BY_PARAM);
        if (value.startsWith("-")) {
            specifications.add(new SortCertificatesDescending(orderByQueries.get(value)));
        } else {
            specifications.add(new SortCertificatesAscending(orderByQueries.get(value)));
        }
    }
}
