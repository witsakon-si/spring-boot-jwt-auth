package com.jwtauth.exception;

public class RefreshTokenExpireException extends RuntimeException {
  public RefreshTokenExpireException(String refreshToken) {
    super(refreshToken);
  }
}
