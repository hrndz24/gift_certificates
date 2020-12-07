package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.Tag;
import org.hibernate.JDBCException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TagDAOImpl implements TagDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Tag addTag(Tag tag) {
        try {
            entityManager.persist(tag);
        } catch (PersistenceException e) {
            throw new DAOException(DAOExceptionCode.FAILED_ADD_TAG.getErrorCode(), e);
        }
        return tag;
    }

    @Override
    public void removeTag(int tagId) {
        Tag tag = entityManager.find(Tag.class, tagId);
        try {
            entityManager.remove(tag);
        } catch (JDBCException e) {
            throw new DAOException(DAOExceptionCode.FAILED_REMOVE_TAG.getErrorCode(), e);
        }
    }

    @Override
    public List<Tag> getTags(int limit, int offset) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(DAOConstant.ID_FIELD.getValue())));
        return entityManager.createQuery(criteriaQuery)
                .setMaxResults(limit).setFirstResult(offset).getResultList();
    }

    @Override
    public Tag getTagById(int id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public Tag getTagByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get(DAOConstant.NAME_FIELD.getValue()), name));
        return entityManager.createQuery(criteriaQuery).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public long getCount() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> count = criteriaBuilder.createQuery(Long.class);
        count.select(criteriaBuilder.count(count.from(Tag.class)));
        return entityManager.createQuery(count).getSingleResult();
    }

    @Override
    public Tag getMostUsedTagOfUserWithHighestCostOfOrders() {
        return (Tag) entityManager.createNativeQuery(DAOConstant.GET_MOST_USED_TAG_QUERY.getValue(), Tag.class).getSingleResult();
    }
}
