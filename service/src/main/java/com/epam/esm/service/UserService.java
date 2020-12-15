package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UsersDTO;

import java.util.Map;

public interface UserService {

    UserDTO addUser(UserDTO userDTO);

    UsersDTO getUsers(Map<String, String> params);

    UserDTO getUserById(int id);

    long getCount();

    UserDTO getUserByEmail(String email);
}
