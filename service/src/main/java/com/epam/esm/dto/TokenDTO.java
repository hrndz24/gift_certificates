package com.epam.esm.dto;

import java.util.Objects;

public class TokenDTO {

    private String username;
    private String token;
    private long validityInMilliseconds;

    public TokenDTO() {
    }

    public TokenDTO(String username, String token, long validityInMilliseconds) {
        this.username = username;
        this.token = token;
        this.validityInMilliseconds = validityInMilliseconds;
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

    public long getValidityInMilliseconds() {
        return validityInMilliseconds;
    }

    public void setValidityInMilliseconds(long validityInMilliseconds) {
        this.validityInMilliseconds = validityInMilliseconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenDTO tokenDTO = (TokenDTO) o;
        return username.equals(tokenDTO.username) &&
                token.equals(tokenDTO.token) &&
                validityInMilliseconds == tokenDTO.validityInMilliseconds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, token, validityInMilliseconds);
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
                "username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", validityInMilliseconds=" + validityInMilliseconds +
                '}';
    }
}
