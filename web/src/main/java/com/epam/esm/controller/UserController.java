package com.epam.esm.controller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.UserService;
import com.epam.esm.util.ControllerPaginationPreparer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller used to implement GET operations on
 * {@code User} data
 */
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;
    private ControllerPaginationPreparer paginationPreparer;

    @Autowired
    public UserController(UserService userService,
                          ControllerPaginationPreparer paginationPreparer) {
        this.userService = userService;
        this.paginationPreparer = paginationPreparer;
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
        users.forEach(user -> {
            user.add(linkTo(methodOn(UserController.class)
                    .getUserById(user.getId()))
                    .withSelfRel());
        });
        int currentPage = Integer.parseInt(params.get("page"));
        long usersCount = userService.getCount();
        List<Link> links = paginationPreparer.prepareLinks(
                methodOn(UserController.class).getUsers(params), params, currentPage, usersCount);
        Map<String, Long> page = paginationPreparer.preparePageInfo(params, currentPage, usersCount);
        CollectionModel<UserDTO> collectionModel = CollectionModel.of(users);
        return HalModelBuilder.halModelOf(collectionModel).links(links).embed(page, LinkRelation.of("page")).build();
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
        userDTO.add(linkTo(methodOn(UserController.class)
                .getUserById(id))
                .withSelfRel());
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(id));
        userDTO.add(linkTo(methodOn(OrderController.class)
                .getAllOrders(params))
                .withRel("orders"));
        return userDTO;
    }
}
