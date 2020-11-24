package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.Tag;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TagDAOImpl implements TagDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public TagDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Tag addTag(Tag tag) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.persist(tag);
        } catch (PersistenceException e) {
            throw new DAOException(DAOExceptionCode.FAILED_ADD_TAG.getErrorCode(), e);
        }
        return tag;
    }

    @Override
    public void removeTag(int tagId) {
        Session session = sessionFactory.getCurrentSession();
        Tag tag = session.load(Tag.class, tagId);
        try {
            session.delete(tag);
        } catch (JDBCException e) {
            throw new DAOException(DAOExceptionCode.FAILED_REMOVE_TAG.getErrorCode(), e);
        }
    }

    @Override
    public List<Tag> getTags(int limit, int offset) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
        return session.createQuery(criteriaQuery).setMaxResults(limit).setFirstResult(offset).getResultList();
    }

    @Override
    public Tag getTagById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Tag.class, id);
    }

    @Override
    public Tag getTagByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("name"), name));
        return session.createQuery(criteriaQuery).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public long getCount() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> count = criteriaBuilder.createQuery(Long.class);
        count.select(criteriaBuilder.count(count.from(Tag.class)));
        return session.createQuery(count).getSingleResult();
    }

    @Override
    public Tag getMostUsedTagOfUserWithHighestCostOfOrders() {
        Session session = sessionFactory.getCurrentSession();
        return session.createNativeQuery(NativeQuery.GET_MOST_USED_TAG.getQuery(), Tag.class).getSingleResult();
    }
}
