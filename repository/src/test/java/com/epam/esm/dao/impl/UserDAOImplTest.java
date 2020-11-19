package com.epam.esm.dao.impl;

import com.epam.esm.model.User;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DAOTestConfig.class)
@SpringBootTest
@Transactional
class UserDAOImplTest {

    private UserDAOImpl userDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOImpl(sessionFactory);
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
}
