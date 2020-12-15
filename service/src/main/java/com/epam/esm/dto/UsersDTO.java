package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

public class UsersDTO extends RepresentationModel<UsersDTO> {

    private List<UserDTO> users;

    public UsersDTO() {
    }

    public UsersDTO(List<UserDTO> users) {
        this.users = users;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UsersDTO usersDTO = (UsersDTO) o;
        return Objects.equals(users, usersDTO.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), users);
    }

    @Override
    public String toString() {
        return "UsersDTO{" +
                "users=" + users +
                '}';
    }
}
