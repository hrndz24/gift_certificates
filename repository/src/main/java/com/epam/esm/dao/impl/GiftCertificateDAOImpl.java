package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.specification.SearchConditionSpecification;
import com.epam.esm.specification.SortSpecification;
import com.epam.esm.specification.Specification;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public GiftCertificate addCertificate(GiftCertificate certificate) {
        try {
            return entityManager.merge(certificate);
        } catch (PersistenceException e) {
            throw new DAOException(DAOExceptionCode.FAILED_ADD_CERTIFICATE.getErrorCode(), e);
        }
    }

    @Override
    public void removeCertificate(int certificateId) {
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, certificateId);
        try {
            entityManager.remove(certificate);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_REMOVE_CERTIFICATE.getErrorCode(), e);
        }
    }

    @Override
    public void updateCertificate(GiftCertificate certificate) {
        try {
            entityManager.find(GiftCertificate.class, certificate.getId());
            entityManager.merge(certificate);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_UPDATE_CERTIFICATE.getErrorCode(), e);
        }
    }

    @Override
    public List<GiftCertificate> getCertificates(List<Specification> specifications, int limit, int offset) {
        try {
            return entityManager.createQuery(buildCriteriaQuery(specifications))
                    .setFirstResult(offset).setMaxResults(limit).getResultList();
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_CERTIFICATES.getErrorCode(), e);
        }
    }

    private CriteriaQuery<GiftCertificate> buildCriteriaQuery(List<Specification> specifications) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        List<Predicate> predicateList = new ArrayList<>();
        specifications.forEach(specification -> {
            if (specification instanceof SearchConditionSpecification)
                predicateList.add(((SearchConditionSpecification) specification).toPredicate(criteriaBuilder, root));
            if (specification instanceof SortSpecification)
                criteriaQuery.orderBy(((SortSpecification) specification).toOrder(criteriaBuilder, root));
        });
        criteriaQuery.where(predicateList.toArray(new Predicate[0]));
        return criteriaQuery;
    }

    @Override
    public GiftCertificate getCertificateById(int id) {
        try {
            return entityManager.find(GiftCertificate.class, id);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_CERTIFICATE.getErrorCode(), e);
        }
    }

    @Override
    public long getCount(List<Specification> specifications) {
        CriteriaQuery<GiftCertificate> criteriaQuery = buildCriteriaQuery(specifications);
        return entityManager.createQuery(criteriaQuery).getResultStream().count();
    }
}
