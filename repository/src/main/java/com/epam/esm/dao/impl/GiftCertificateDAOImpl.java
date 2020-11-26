package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.GiftCertificate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
    public List<GiftCertificate> getCertificates(CriteriaQuery<GiftCertificate> criteriaQuery, int limit, int offset) {
        try {
            return entityManager.createQuery(criteriaQuery).setMaxResults(limit).setFirstResult(offset).getResultList();
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_CERTIFICATES.getErrorCode(), e);
        }
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
    public long getCount(CriteriaQuery<GiftCertificate> criteriaQuery) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> count = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> root = count.from(GiftCertificate.class);
        root.join("tags").alias("tagRoot");
        root.alias("certificateAlias");
        count.select(criteriaBuilder.count(root));
        if (criteriaQuery.getRestriction() != null)
            count.where(criteriaQuery.getRestriction());
        return entityManager.createQuery(count).getSingleResult();
    }
}
