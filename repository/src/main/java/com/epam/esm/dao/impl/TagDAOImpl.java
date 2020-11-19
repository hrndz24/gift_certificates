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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;

@Repository
@Transactional
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
    @SuppressWarnings("unchecked assignment")
    public List<Tag> getTags() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Tag").list();
    }

    @Override
    public Tag getTagById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Tag.class, id);
    }
}
