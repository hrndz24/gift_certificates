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

    private static final String ORDER_BY_PARAM = "orderBy";
    private static final String TAG_NAME_PARAM = "tagName";
    private static final String CERTIFICATE_NAME_PARAM = "certificateName";
    private static final String CERTIFICATE_DESCRIPTION_PARAM = "certificateDescription";

    private CriteriaQuery<GiftCertificate> criteria;

    @Autowired
    public GiftCertificateQueryGenerator(SessionFactory sessionFactory) {
        orderByQueries = new HashMap<>();
        this.sessionFactory = sessionFactory;
        fillInQueries();
    }

    private void fillInQueries() {
        orderByQueries.put(ServiceConstant.SORT_BY_NAME_ASC.getValue(), ServiceConstant.NAME_FIELD.getValue());
        orderByQueries.put(ServiceConstant.SORT_BY_NAME_DESC.getValue(), ServiceConstant.NAME_FIELD.getValue());
        orderByQueries.put(ServiceConstant.SORT_BY_DATE_ASC.getValue(), ServiceConstant.CREATED_DATE_FIELD.getValue());
        orderByQueries.put(ServiceConstant.SORT_BY_DATE_DESC.getValue(), ServiceConstant.CREATED_DATE_FIELD.getValue());
    }

    public CriteriaQuery<GiftCertificate> generateQueryCriteria(Map<String, String> params) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        criteria = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteria.from(GiftCertificate.class);
        root.alias("certificateAlias");
        criteria.select(root);
        addQueryPredicates(params, criteriaBuilder, root);
        return criteria;
    }

    private void addQueryPredicates(Map<String, String> params, CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        List<Predicate> predicateList = new ArrayList<>();
        params.keySet().forEach(key -> {
            switch (key) {
                case CERTIFICATE_NAME_PARAM:
                    predicateList.add(criteriaBuilder.like(criteriaBuilder.lower
                                    (root.get(ServiceConstant.NAME_FIELD.getValue())),
                            "%" + params.get(key).toLowerCase() + "%"));
                    break;
                case CERTIFICATE_DESCRIPTION_PARAM:
                    predicateList.add(criteriaBuilder.like(criteriaBuilder.lower
                                    (root.get(ServiceConstant.DESCRIPTION_FIELD.getValue())),
                            "%" + params.get(key).toLowerCase() + "%"));
                    break;
                case ORDER_BY_PARAM:
                    addOrderBy(params, criteriaBuilder, root);
                    break;
                case TAG_NAME_PARAM:
                    predicateList.addAll(addTagNamePredicate(params, criteriaBuilder, root));
                    break;
                default:
                    break;
            }
        });
        Predicate[] predicates = new Predicate[0];
        criteria.where(predicateList.toArray(predicates));
    }

    private List<Predicate> addTagNamePredicate(Map<String, String> params, CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        List<Predicate> predicates = new ArrayList<>();
        String[] tags = params.get(TAG_NAME_PARAM).split(ServiceConstant.TAGS_TO_SEARCH_BY_SEPARATOR.getValue());
        Join<Object, Object> join = root.join(ServiceConstant.TAGS_FIELD.getValue());
        join.alias("tagRoot");
        for (String tag : tags) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(
                    join.get(ServiceConstant.NAME_FIELD.getValue())), "%" + tag.toLowerCase() + "%"));
        }
        return predicates;
    }

    private void addOrderBy(Map<String, String> params, CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        Order orderBy;
        String value = params.get(ORDER_BY_PARAM);
        if (value.startsWith("-")) {
            orderBy = criteriaBuilder.desc(root.get(orderByQueries.get(value)));
        } else {
            orderBy = criteriaBuilder.asc(root.get(orderByQueries.get(value)));
        }
        criteria.orderBy(orderBy);
    }
}
