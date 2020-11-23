package com.epam.esm.utils;

import com.epam.esm.model.Order;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Map;

@Component
public class OrderQueryGenerator {

    private SessionFactory sessionFactory;

    private CriteriaQuery<Order> criteria;

    @Autowired
    public OrderQueryGenerator(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
            if (key.equals(ServiceConstant.USER_ID_PARAM.getValue())) {
                criteria.where(criteriaBuilder.equal(root.get(key), params.get(key)));
            }
        });
    }
}
