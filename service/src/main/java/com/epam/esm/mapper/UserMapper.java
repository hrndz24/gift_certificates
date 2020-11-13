package com.epam.esm.mapper;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserMapper {

    private ModelMapper mapper;

    @Autowired
    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public User toModel(UserDTO dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, User.class);
    }

    public UserDTO toDTO(User model) {
        return Objects.isNull(model) ? null : mapper.map(model, UserDTO.class);
    }
}
