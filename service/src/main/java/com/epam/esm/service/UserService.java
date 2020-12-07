package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<UserDTO> getUsers(Map<String, String> params);

    UserDTO getUserById(int id);

    long getCount();

    UserDTO getUserByEmail(String email);
}
