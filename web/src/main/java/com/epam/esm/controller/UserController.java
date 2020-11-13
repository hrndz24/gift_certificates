package com.epam.esm.controller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller used to implement GET operations on
 * {@code User} data
 */
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns list of {@code UserDTO}
     * that represent all users in the database.
     *
     * @return list of UserDTOs corresponding to users in the database
     */
    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    /**
     * Returns {@code UserDTO} requested by id.
     * If no resource found {@code HttpStatus.NOT_FOUND} is returned.
     *
     * @param id id of the requested user
     * @return UserDTO with the requested id
     */
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }
}
