package com.epam.esm.specification.impl.order;

import com.epam.esm.dao.impl.DAOConstant;
import com.epam.esm.specification.SearchConditionSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GetOrdersByUserId implements SearchConditionSpecification {

    private int userId;

    public GetOrdersByUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public <T> Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root<T> root) {
        return criteriaBuilder.equal(root.get(DAOConstant.USER_ID_FIELD.getValue()), userId);
    }
}
