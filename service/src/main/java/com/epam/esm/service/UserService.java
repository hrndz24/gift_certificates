package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getUsers();

    UserDTO getUserById(int id);
}
