package com.epam.esm.dao.impl;

import com.epam.esm.model.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class OrderDAOImplTest {

    private EmbeddedDatabase embeddedDatabase;

    private OrderDAOImpl orderDAO;

    private static final String GET_ALL_ORDERS_QUERY = "";
    private static final String GET_ORDERS_BY_USER_QUERY = " WHERE user_id = 2";

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        orderDAO = new OrderDAOImpl(jdbcTemplate);
    }

    @Test
    void addOrderShouldAddOrder() {
        Order newOrder = new Order();
        newOrder.setUserId(1);
        newOrder.setCost(new BigDecimal("45.00"));
        newOrder.setDate(new Date());
        orderDAO.addOrder(newOrder);
        assertNotEquals(0, newOrder.getId());
    }

    @Test
    void getOrdersShouldReturnListOfThreeOrders() {
        assertEquals(3, orderDAO.getOrders(GET_ALL_ORDERS_QUERY).size());
    }

    @Test
    void getOrdersByUserIdShouldReturnListOfTwo() {
        assertEquals(2, orderDAO.getOrders(GET_ORDERS_BY_USER_QUERY).size());
    }

    @Test
    void getOrderByIdWithExistingIdShouldReturnOrder() {
        assertEquals(new BigDecimal("45.50"), orderDAO.getOrderById(3).getCost());
    }

    @Test
    void getOrderByIdWithNonExistingIdShouldReturnNull() {
        assertNull(orderDAO.getOrderById(4));
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }
}