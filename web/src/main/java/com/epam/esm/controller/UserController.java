package com.epam.esm.controller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.jwt.JwtProvider;
import com.epam.esm.jwt.JwtUser;
import com.epam.esm.service.UserService;
import com.epam.esm.util.HateoasBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, HateoasBuilder hateoasBuilder, AuthenticationManager authenticationManager, JwtProvider jwtTokenProvider) {
        this.userService = userService;
        this.hateoasBuilder = hateoasBuilder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            String username = userDTO.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userDTO.getPassword()));
            UserDTO user = userService.getUserByEmail(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(user);

            /*Map<Object, Object> response = new HashMap<>();
            response.put("email", username);
            response.put("token", token);*/

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    /**
     * Returns list of {@code UserDTO}
     * that represent all users in the database.
     *
     * @return list of UserDTOs corresponding to users in the database
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
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
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public UserDTO getUserById(@PathVariable("id") int id) {
        /*JwtUser principal = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.getId() != id) {
            throw new BadCredentialsException("Not allowed");
        }*/
        UserDTO userDTO = userService.getUserById(id);
        return hateoasBuilder.addLinksForUserDTO(userDTO);
    }
}
