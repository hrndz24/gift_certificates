package com.epam.esm.utils;

import com.epam.esm.model.GiftCertificate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GiftCertificateQueryGenerator {

    private Map<String, String> orderByQueries;
    private SessionFactory sessionFactory;

    private static final String ORDER_BY_PARAM_NAME = "orderBy";
    private static final String TAG_NAME_PARAM_NAME = "tagName";

    private static final String NAME_FIELD = "name";
    private static final String DATE_FIELD = "createDate";

    private CriteriaQuery<GiftCertificate> criteria;

    @Autowired
    public GiftCertificateQueryGenerator(SessionFactory sessionFactory) {
        orderByQueries = new HashMap<>();
        this.sessionFactory = sessionFactory;
        fillInQueries();
    }

    private void fillInQueries() {
        orderByQueries.put("name", NAME_FIELD);
        orderByQueries.put("-name", NAME_FIELD);
        orderByQueries.put("date", DATE_FIELD);
        orderByQueries.put("-date", DATE_FIELD);
    }

    public CriteriaQuery<GiftCertificate> generateQueryCriteria(Map<String, String> params) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        criteria = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteria.from(GiftCertificate.class);
        criteria.select(root);
        addQueryPredicates(params, criteriaBuilder, root);
        return criteria;
    }

    private void addQueryPredicates(Map<String, String> params, CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        List<Predicate> predicateList = new ArrayList<>();
        params.keySet().forEach(key -> {
            if ("certificateName".equals(key)) {
                predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + params.get(key).toLowerCase() + "%"));
            } else if ("certificateDescription".equals(key)) {
                predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                        "%" + params.get(key).toLowerCase() + "%"));
            } else if (ORDER_BY_PARAM_NAME.equals(key)) {
                addOrderBy(params, criteriaBuilder, root);
            } else if (TAG_NAME_PARAM_NAME.equals(key)) {
                predicateList.addAll(addTagNamePredicate(params, criteriaBuilder, root));
            }
        });
        Predicate[] predicates = new Predicate[0];
        criteria.where(predicateList.toArray(predicates));
    }

    private List<Predicate> addTagNamePredicate(Map<String, String> params, CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        List<Predicate> predicates = new ArrayList<>();
        String[] tags = params.get(TAG_NAME_PARAM_NAME).split(", ");
        for (String tag : tags) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(
                    root.join("tags").get("name")), "%" + tag.toLowerCase() + "%"));
        }
        return predicates;
    }

    private void addOrderBy(Map<String, String> params, CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        Order orderBy;
        String value = params.get(ORDER_BY_PARAM_NAME);
        if (value.startsWith("-")) {
            orderBy = criteriaBuilder.desc(root.get(orderByQueries.get(value)));
        } else {
            orderBy = criteriaBuilder.asc(root.get(orderByQueries.get(value)));
        }
        criteria.orderBy(orderBy);
    }

}
