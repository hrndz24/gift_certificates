package com.epam.esm.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public interface SortSpecification extends Specification {

    <T> Order toOrder(CriteriaBuilder criteriaBuilder, Root<T> root);
}
