package com.epam.esm.specification.impl.certificate;

import com.epam.esm.dao.impl.DAOConstant;
import com.epam.esm.specification.SearchConditionSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GetCertificatesByName implements SearchConditionSpecification {

    private String name;

    public GetCertificatesByName(String name) {
        this.name = name;
    }

    @Override
    public <T> Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root<T> root) {
        return criteriaBuilder.like(criteriaBuilder.lower(
                root.get(DAOConstant.NAME_FIELD.getValue())), "%" + name.toLowerCase() + "%");
    }
}
