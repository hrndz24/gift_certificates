package com.epam.esm.service.impl;

import com.epam.esm.dto.TokenDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.jwt.JwtProvider;
import com.epam.esm.mapper.UserDetailsMapper;
import com.epam.esm.service.AuthorizationService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtTokenProvider;
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    public AuthorizationServiceImpl(UserService userService,
                                    AuthenticationManager authenticationManager,
                                    JwtProvider jwtTokenProvider,
                                    UserDetailsMapper userDetailsMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsMapper = userDetailsMapper;
    }

    public TokenDTO logIn(UserDTO userDTO) {
        String username = userDTO.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userDTO.getPassword()));
        UserDTO user = userService.getUserByEmail(username);
        String token = jwtTokenProvider.createToken(user);
        return new TokenDTO(username, token, jwtTokenProvider.getValidityInMilliseconds());
    }

    @Override
    public TokenDTO signUp(UserDTO userDTO) {
        UserDTO addedUser = userService.addUser(userDTO);
        String token = jwtTokenProvider.createToken(addedUser);
        return new TokenDTO(addedUser.getEmail(), token, jwtTokenProvider.getValidityInMilliseconds());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userService.getUserByEmail(username);
        return userDetailsMapper.toUserDetails(user);
    }
}
