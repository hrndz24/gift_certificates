package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.Order;
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
public class OrderDAOImpl implements OrderDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Order addOrder(Order order) {
        try {
            return entityManager.merge(order);
        } catch (PersistenceException e) {
            throw new DAOException(DAOExceptionCode.FAILED_ADD_ORDER.getErrorCode(), e);
        }
    }

    @Override
    public List<Order> getOrders(CriteriaQuery<Order> criteriaQuery, int limit, int offset) {
        try {
            return entityManager.createQuery(criteriaQuery)
                    .setMaxResults(limit).setFirstResult(offset).getResultList();
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_ORDERS.getErrorCode(), e);
        }
    }

    @Override
    public Order getOrderById(int id) {
        try {
            return entityManager.find(Order.class, id);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_ORDER.getErrorCode(), e);
        }
    }

    @Override
    public long getCount(CriteriaQuery<Order> criteriaQuery) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> count = criteriaBuilder.createQuery(Long.class);
        Root<Order> root = count.from(Order.class);
        root.alias("orderAlias");
        count.select(criteriaBuilder.count(root));
        if (criteriaQuery.getRestriction() != null)
            count.where(criteriaQuery.getRestriction());
        return entityManager.createQuery(count).getSingleResult();
    }
}
