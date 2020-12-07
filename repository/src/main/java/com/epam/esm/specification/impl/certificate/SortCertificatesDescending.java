package com.epam.esm.specification.impl.certificate;

import com.epam.esm.specification.SortSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public class SortCertificatesDescending implements SortSpecification {

    private String field;

    public SortCertificatesDescending(String field) {
        this.field = field;
    }

    @Override
    public <T> Order toOrder(CriteriaBuilder criteriaBuilder, Root<T> root) {
        return criteriaBuilder.desc(root.get(field));
    }
}
