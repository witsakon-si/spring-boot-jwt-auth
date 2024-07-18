package com.jwtauth.service;

import com.jwtauth.entity.RefreshToken;
import com.jwtauth.entity.User;
import com.jwtauth.exception.RefreshTokenExpireException;
import com.jwtauth.repository.RefreshTokenRepository;
import com.jwtauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${security.jwt.refresh-token.expiration-time}")
    private Long refreshTokenExp;

    public String createRefreshToken(Long userId) {
        log.debug("createRefreshToken: {}", userId);
        String token = null;
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            RefreshToken refreshToken = refreshTokenRepository.findByUser(user.get());
            if (refreshToken != null) {
                refreshToken.setToken(UUID.randomUUID().toString())
                        .setExpiryDate(Instant.now().plusMillis(refreshTokenExp));
            } else {
                refreshToken = new RefreshToken()
                        .setUser(userRepository.findById(userId).get())
                        .setToken(UUID.randomUUID().toString())
                        .setExpiryDate(Instant.now().plusMillis(refreshTokenExp));
            }
            refreshTokenRepository.save(refreshToken);
            token = refreshToken.getToken();
        }

        return token;
    }

    public Optional<RefreshToken> findByToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpireException(token.getToken());
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(User user) {
        int status = refreshTokenRepository.deleteByUser(user);
        jwtService.revokeAccessToken(user.getUsername());
        return status;
    }
}
