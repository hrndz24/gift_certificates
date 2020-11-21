package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
@Transactional
public class OrderDAOImpl implements OrderDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public OrderDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Order addOrder(Order order) {
        Session session = sessionFactory.openSession();
        int id;
        try {
            session.beginTransaction();
            id = ((Order) session.merge(order)).getId();
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new DAOException(DAOExceptionCode.FAILED_ADD_ORDER.getErrorCode(), e);
        }
        return sessionFactory.openSession().get(Order.class, id);
    }

    @Override
    public List<Order> getOrders(CriteriaQuery<Order> criteriaQuery, int limit, int offset) {
        try {
            return sessionFactory.getCurrentSession().createQuery(criteriaQuery)
                    .setMaxResults(limit).setFirstResult(offset).list();
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_ORDERS.getErrorCode(), e);
        }
    }

    @Override
    public Order getOrderById(int id) {
        try {
            return sessionFactory.getCurrentSession().get(Order.class, id);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_ORDER.getErrorCode(), e);
        }
    }
}
