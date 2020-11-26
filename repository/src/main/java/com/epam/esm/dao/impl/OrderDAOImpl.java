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
import java.util.List;
import java.util.stream.Collectors;

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
    @SuppressWarnings("unchecked assignment")
    public List<Order> getOrders(String queryCondition, int limit, int offset) {
        try {
            return (List<Order>) entityManager.createNativeQuery(
                    NativeQuery.GET_ORDERS.getQuery() + queryCondition, Order.class)
                    .setMaxResults(limit).setFirstResult(offset).getResultStream().distinct().collect(Collectors.toList());
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
    public long getCount(String queryCondition) {
        return entityManager.createNativeQuery(
                NativeQuery.GET_ORDERS.getQuery() + queryCondition, Order.class)
                .getResultStream().distinct().count();
    }
}
