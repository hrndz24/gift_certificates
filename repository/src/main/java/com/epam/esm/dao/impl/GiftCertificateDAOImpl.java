package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.GiftCertificate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public GiftCertificateDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public GiftCertificate addCertificate(GiftCertificate certificate) {
        Session session = sessionFactory.openSession();
        int id;
        try {
            session.beginTransaction();
            id = ((GiftCertificate) session.merge(certificate)).getId();
            session.getTransaction().commit();
            session.close();
        } catch (PersistenceException e) {
            session.getTransaction().rollback();
            session.close();
            throw new DAOException(DAOExceptionCode.FAILED_ADD_CERTIFICATE.getErrorCode(), e);
        }
        return sessionFactory.openSession().get(GiftCertificate.class, id);
    }

    @Override
    public void removeCertificate(int certificateId) {
        Session session = sessionFactory.getCurrentSession();
        GiftCertificate certificate = session.load(GiftCertificate.class, certificateId);
        try {
            session.delete(certificate);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_REMOVE_CERTIFICATE.getErrorCode(), e);
        }
    }

    @Override
    public void updateCertificate(GiftCertificate certificate) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.load(GiftCertificate.class, certificate.getId());
            session.merge(certificate);
            session.getTransaction().commit();
            session.close();
        } catch (DataAccessException e) {
            session.getTransaction().rollback();
            session.close();
            throw new DAOException(DAOExceptionCode.FAILED_UPDATE_CERTIFICATE.getErrorCode(), e);
        }
    }

    @Override
    public List<GiftCertificate> getCertificates(CriteriaQuery<GiftCertificate> criteriaQuery, int limit, int offset) {
        Session session = sessionFactory.getCurrentSession();
        try {
            return session.createQuery(criteriaQuery).setMaxResults(limit).setFirstResult(offset).list();
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_CERTIFICATES.getErrorCode(), e);
        }
    }

    @Override
    public GiftCertificate getCertificateById(int id) {
        Session session = sessionFactory.getCurrentSession();
        try {
            return session.get(GiftCertificate.class, id);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_CERTIFICATE.getErrorCode(), e);
        }
    }

    @Override
    public long getCount(CriteriaQuery<GiftCertificate> criteriaQuery) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> count = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> root = count.from(GiftCertificate.class);
        root.join("tags").alias("tagRoot");
        root.alias("certificateAlias");
        count.select(criteriaBuilder.count(root));
        if (criteriaQuery.getRestriction() != null)
            count.where(criteriaQuery.getRestriction());
        return sessionFactory.getCurrentSession().createQuery(count).getSingleResult();
    }
}
