package com.jwtauth.controller;

import com.jwtauth.dto.LoginUserDto;
import com.jwtauth.dto.RefreshTokenDto;
import com.jwtauth.dto.RegisterUserDto;
import com.jwtauth.entity.RefreshToken;
import com.jwtauth.entity.User;
import com.jwtauth.exception.RefreshTokenNotFoundException;
import com.jwtauth.response.LoginResponse;
import com.jwtauth.service.AuthenticationService;
import com.jwtauth.service.JwtService;
import com.jwtauth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody RegisterUserDto registerUserDto) {
        authenticationService.signup(registerUserDto);

        return ResponseEntity.ok("Sign Up successful!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        String refreshToken = refreshTokenService.createRefreshToken(authenticatedUser.getId());

        LoginResponse loginResponse = new LoginResponse(jwtToken, refreshToken);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
                }).orElseThrow(() -> new RefreshTokenNotFoundException(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        // TODO: Revoke active access token
        refreshTokenService.findByToken(refreshToken)
                .map(token -> refreshTokenService.deleteByUserId(token.getUser().getId()))
                .orElseThrow(() -> new RefreshTokenNotFoundException(refreshToken));
        return ResponseEntity.ok("Logout successful!");
    }
}
