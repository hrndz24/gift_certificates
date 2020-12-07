package com.epam.esm.specification.impl.certificate;

import com.epam.esm.dao.impl.DAOConstant;
import com.epam.esm.specification.SearchConditionSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GetCertificatesByDescription implements SearchConditionSpecification {

    private String description;

    public GetCertificatesByDescription(String description) {
        this.description = description;
    }

    @Override
    public <T> Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root<T> root) {
        return criteriaBuilder.like(criteriaBuilder.lower(
                root.get(DAOConstant.DESCRIPTION_FIELD.getValue())), "%" + description.toLowerCase() + "%");
    }
}
