package com.epam.esm.dao.impl;

import com.epam.esm.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DAOTestConfig.class)
@SpringBootTest
@Transactional
class OrderDAOImplTest {

    @Autowired
    private OrderDAOImpl orderDAO;

    private static final String GET_ALL_CONDITION = "";
    private static final String GET_ORDERS_BY_USER_CONDITION = " WHERE user_id = 2";

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
        assertEquals(3, orderDAO.getOrders(GET_ALL_CONDITION, 10, 0).size());
    }

    @Test
    void getOrdersByUserIdShouldReturnListOfTwo() {
        assertEquals(2, orderDAO.getOrders(GET_ORDERS_BY_USER_CONDITION, 10, 0).size());
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