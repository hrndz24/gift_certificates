package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getUsers(int limit, int offset) {
        try {
            Session session = sessionFactory.getCurrentSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            criteriaQuery.from(User.class);
            return session.createQuery(criteriaQuery).setMaxResults(limit).setFirstResult(offset).getResultList();
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_USERS.getErrorCode(), e);
        }
    }

    @Override
    public User getUserById(int id) {
        try {
            return sessionFactory.getCurrentSession().get(User.class, id);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_USER.getErrorCode(), e);
        }
    }

    @Override
    public long getCount() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> count = criteriaBuilder.createQuery(Long.class);
        count.select(criteriaBuilder.count(count.from(User.class)));
        return session.createQuery(count).getSingleResult();
    }
}
