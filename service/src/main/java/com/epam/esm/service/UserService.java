package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserDTO addUser(UserDTO userDTO);

    List<UserDTO> getUsers(Map<String, String> params);

    UserDTO getUserById(int id);

    long getCount();

    UserDTO getUserByEmail(String email);
}
