package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UserDAOImpl implements UserDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked assignment")
    public List<User> getUsers() {
        try {
            return sessionFactory.getCurrentSession().createQuery("from User ").list();
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
}
