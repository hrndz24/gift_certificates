package com.epam.esm.specification.impl.certificate;

import com.epam.esm.dao.impl.DAOConstant;
import com.epam.esm.specification.SearchConditionSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GetCertificatesByTagName implements SearchConditionSpecification {

    private String tagName;

    public GetCertificatesByTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public <T> Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root<T> root) {
        return criteriaBuilder.like(criteriaBuilder.lower(
                root.join(DAOConstant.TAGS_FIELD.getValue()).get(DAOConstant.NAME_FIELD.getValue())),
                "%" + tagName.toLowerCase() + "%");
    }
}
