package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.Order;
import com.epam.esm.specification.SearchConditionSpecification;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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
    public List<Order> getOrders(List<SearchConditionSpecification> specifications, int limit, int offset) {
        try {
            return entityManager.createQuery(buildCriteriaQuery(specifications))
                    .setMaxResults(limit).setFirstResult(offset).getResultList();
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_ORDERS.getErrorCode(), e);
        }
    }

    private CriteriaQuery<Order> buildCriteriaQuery(List<SearchConditionSpecification> specifications) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        List<Predicate> predicateList = new ArrayList<>();
        specifications.forEach(specification -> {
            predicateList.add(specification.toPredicate(criteriaBuilder, root));
        });
        criteriaQuery.where(predicateList.toArray(new Predicate[0]));
        return criteriaQuery;
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
    public long getCount(List<SearchConditionSpecification> specifications) {
        CriteriaQuery<Order> criteriaQuery = buildCriteriaQuery(specifications);
        return entityManager.createQuery(criteriaQuery).getResultStream().count();
    }
}
