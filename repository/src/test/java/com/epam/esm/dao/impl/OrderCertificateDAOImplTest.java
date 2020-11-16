package com.epam.esm.dao.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.junit.jupiter.api.Assertions.*;

class OrderCertificateDAOImplTest {

    private EmbeddedDatabase embeddedDatabase;

    private OrderCertificateDAOImpl orderCertificateDAO;
    private OrderDAOImpl orderDAO;

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        orderCertificateDAO = new OrderCertificateDAOImpl(jdbcTemplate);
        orderDAO = new OrderDAOImpl(jdbcTemplate);
    }

    @Test
    void addCertificateToOrderShouldAddCertificateToOrder() {
        orderCertificateDAO.addCertificateToOrder(3, 2);
        assertEquals(2, orderDAO.getOrderById(3).getCertificates().size());
    }

    @Test
    void isCertificateAssignedToOrderWithAssignedCertificateShouldReturnTrue() {
        assertTrue(orderCertificateDAO.isCertificateAssignedToOrder(3, 1));
    }

    @Test
    void isCertificateAssignedToOrder() {
        assertFalse(orderCertificateDAO.isCertificateAssignedToOrder(3, 2));
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }
}