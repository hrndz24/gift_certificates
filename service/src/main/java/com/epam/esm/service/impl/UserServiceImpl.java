package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.exception.ValidatorException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import com.epam.esm.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;
    private UserMapper userMapper;
    private Validator validator;

    @Autowired
    public UserServiceImpl(UserDAO userDAO,
                           UserMapper userMapper,
                           Validator validator) {
        this.userDAO = userDAO;
        this.userMapper = userMapper;
        this.validator = validator;
    }

    @Override
    public List<UserDTO> getUsers() {
        List<UserDTO> userDTOS = new ArrayList<>();
        userDAO.getUsers().forEach(user -> userDTOS.add(userMapper.toDTO(user)));
        return userDTOS;
    }

    @Override
    public UserDTO getUserById(int id) {
        validator.checkIdIsPositive(id);
        User user = getUserByIdIfExists(id);
        return userMapper.toDTO(user);
    }

    private User getUserByIdIfExists(int id) {
        User user = userDAO.getUserById(id);
        if (user == null)
            throw new ValidatorException(
                    ServiceExceptionCode.NON_EXISTING_USER_ID.getErrorCode(), String.valueOf(id));
        return user;
    }
}
