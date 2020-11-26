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
import java.util.List;
import java.util.stream.Collectors;

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
    @SuppressWarnings("unchecked assignment")
    public List<GiftCertificate> getCertificates(String queryCondition, int limit, int offset) {
        try {
            return (List<GiftCertificate>) entityManager.createNativeQuery(
                    NativeQuery.GET_CERTIFICATES.getQuery() + queryCondition, GiftCertificate.class)
                    .setFirstResult(offset).setMaxResults(limit).getResultStream().distinct().collect(Collectors.toList());
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
    public long getCount(String queryCondition) {
        return entityManager.createNativeQuery(
                NativeQuery.GET_CERTIFICATES.getQuery() + queryCondition, GiftCertificate.class)
                .getResultStream().distinct().count();
    }
}
