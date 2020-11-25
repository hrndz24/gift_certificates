package com.epam.esm.controller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.UserService;
import com.epam.esm.util.HateoasBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller used to implement GET operations on
 * {@code User} data
 */
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;
    private HateoasBuilder hateoasBuilder;

    @Autowired
    public UserController(UserService userService,
                          HateoasBuilder hateoasBuilder) {
        this.userService = userService;
        this.hateoasBuilder = hateoasBuilder;
    }

    /**
     * Returns list of {@code UserDTO}
     * that represent all users in the database.
     *
     * @return list of UserDTOs corresponding to users in the database
     */
    @GetMapping
    public RepresentationModel<?> getUsers(@RequestParam Map<String, String> params) {
        List<UserDTO> users = userService.getUsers(params);
        long usersCount = userService.getCount();
        return hateoasBuilder.addLinksForListOfUserDTOs(users, params, usersCount);
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
        UserDTO userDTO = userService.getUserById(id);
        return hateoasBuilder.addLinksForUserDTO(userDTO);
    }
}
