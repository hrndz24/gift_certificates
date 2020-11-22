package com.epam.esm.utils;

import com.epam.esm.model.Order;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;

@Component
public class OrderQueryGenerator {

    private Map<String, String> queries;
    private SessionFactory sessionFactory;

    private static final String GET_BY_USER_ID = " WHERE user_id = ?";

    private CriteriaQuery<Order> criteria;

    @Autowired
    public OrderQueryGenerator(SessionFactory sessionFactory) {
        queries = new HashMap<>();
        this.sessionFactory = sessionFactory;
        fillInQueries();
    }

    private void fillInQueries() {
        queries.put("userId", GET_BY_USER_ID);
    }

    public CriteriaQuery<Order> generateQuery(Map<String, String> params) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        criteria = criteriaBuilder.createQuery(Order.class);
        appendPredicates(params, criteriaBuilder);
        return criteria;
    }

    private void appendPredicates(Map<String, String> params, CriteriaBuilder criteriaBuilder) {
        Root<Order> root = criteria.from(Order.class);
        root.alias("orderAlias");
        params.keySet().forEach(key -> {
            if (key.equals("userId")) {
                criteria.where(criteriaBuilder.equal(root.get(key), params.get(key)));
            }
        });
    }
}
