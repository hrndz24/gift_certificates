package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.exception.ValidatorException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.User;
import com.epam.esm.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserDAO userDAO;
    @Spy
    private UserMapper userMapper = new UserMapper(new ModelMapper());
    @Spy
    private Validator validator = new Validator();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsersShouldReturnListOfThreeUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        when(userDAO.getUsers(10, 0)).thenReturn(users);
        doNothing().when(validator).validatePageNumberIsLessThanElementsCount(anyMap(), anyLong());
        assertEquals(3, userService.getUsers(new HashMap<>()).size());
    }

    @Test
    void getUserByIdWithExistentIdShouldReturnUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("natik@nat.com");
        when(userDAO.getUserById(1)).thenReturn(user);
        assertEquals("natik@nat.com", userService.getUserById(1).getEmail());
    }

    @Test
    void getUserByIdWithNonExistentIdShouldThrowException() {
        when(userDAO.getUserById(4)).thenReturn(null);
        assertThrows(ValidatorException.class, () -> userService.getUserById(4));
    }
}