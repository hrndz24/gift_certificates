package com.epam.esm.dao;

import com.epam.esm.model.User;

import java.util.List;

public interface UserDAO {

    User addUser(User user);

    List<User> getUsers(int limit, int offset);

    User getUserById(int id);

    long getCount();

    User getUserByEmail(String email);
}
