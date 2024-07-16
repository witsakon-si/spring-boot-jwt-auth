package com.jwtauth.response;

import lombok.Getter;

public class LoginResponse {
    @Getter
    private String token;

    @Getter
    private long expiresIn;

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
}
