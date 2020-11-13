package com.epam.esm.dao.impl;

import com.epam.esm.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserDAOImplTest {

    private static EmbeddedDatabase embeddedDatabase;

    private UserDAOImpl userDAO;

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        userDAO = new UserDAOImpl(jdbcTemplate);
    }

    @Test
    void getUsersShouldReturnListOfThreeUsers() {
        assertEquals(3, userDAO.getUsers().size());
    }

    @Test
    void getUserByIdWithExistentIdShouldReturnUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("pak@pak.com");
        user.setPassword("ea2f24301114cd3e62e01c41bfbc93f6ba49f6d8cb3a6e080db6186444fb4d283a444cb69eb28d8c6f5166408be8d12c3ea701882231807bf610e8d37aef2907");
        assertEquals(user, userDAO.getUserById(1));
    }

    @Test
    void getUserByIdWithNonExistentIdShouldReturnNull() {
        assertNull(userDAO.getUserById(4));
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }
}