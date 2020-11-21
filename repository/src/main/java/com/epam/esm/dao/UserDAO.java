package com.epam.esm.dao;

import com.epam.esm.model.User;

import java.util.List;

public interface UserDAO {

    List<User> getUsers(int limit, int offset);

    User getUserById(int id);
}
