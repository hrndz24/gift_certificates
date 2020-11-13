package com.epam.esm.dao;

import com.epam.esm.model.User;

import java.util.List;

public interface UserDAO {

    List<User> getUsers();

    User getUserById(int id);
}
