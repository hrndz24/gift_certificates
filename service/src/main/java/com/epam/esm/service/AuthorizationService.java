package com.epam.esm.service;

import com.epam.esm.dto.TokenDTO;
import com.epam.esm.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizationService extends UserDetailsService {

    TokenDTO logIn(UserDTO userDTO);

    TokenDTO signUp(UserDTO userDTO);
}
