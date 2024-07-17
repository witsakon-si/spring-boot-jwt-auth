package com.jwtauth.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
  public RefreshTokenNotFoundException(String refreshToken) {
    super(refreshToken);
  }
}
