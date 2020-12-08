package com.epam.esm.dto;

import java.util.Objects;

public class TokenDTO {

    private String username;
    private String token;

    public TokenDTO() {
    }

    public TokenDTO(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenDTO tokenDTO = (TokenDTO) o;
        return username.equals(tokenDTO.username) &&
                token.equals(tokenDTO.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, token);
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
                "username='" + username + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
