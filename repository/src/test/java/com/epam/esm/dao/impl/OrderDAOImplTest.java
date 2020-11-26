package com.epam.esm.dao.impl;

import com.epam.esm.model.Order;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DAOTestConfig.class)
@SpringBootTest
@Transactional
class OrderDAOImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OrderDAOImpl orderDAO;

    private CriteriaQuery<Order> criteriaQuery;

    @BeforeEach
    void setUp() {
        criteriaQuery = entityManager.getCriteriaBuilder().createQuery(Order.class);
        criteriaQuery.select(criteriaQuery.from(Order.class));
    }

    @Test
    void addOrderShouldAddOrder() {
        Order newOrder = new Order();
        newOrder.setUserId(1);
        newOrder.setCost(new BigDecimal("45.00"));
        newOrder.setDate(new Date());
        Order returnedOrder = orderDAO.addOrder(newOrder);
        assertNotEquals(0, returnedOrder.getId());
    }

    @Test
    void getOrdersShouldReturnListOfThreeOrders() {
        assertEquals(3, orderDAO.getOrders(criteriaQuery, 10, 0).size());
    }

    @Test
    void getOrdersByUserIdShouldReturnListOfTwo() {
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root)
                .where(entityManager.getCriteriaBuilder().equal(root.get("userId"), 2)).distinct(true);
        assertEquals(2, orderDAO.getOrders(criteriaQuery, 10, 0).size());
    }

    @Test
    void getOrderByIdWithExistingIdShouldReturnOrder() {
        assertEquals(new BigDecimal("45.50"), orderDAO.getOrderById(3).getCost());
    }

    @Test
    void getOrderByIdWithNonExistingIdShouldReturnNull() {
        assertNull(orderDAO.getOrderById(4));
    }
}