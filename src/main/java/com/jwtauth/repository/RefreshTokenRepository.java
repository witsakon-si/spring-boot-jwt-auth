package com.jwtauth.repository;

import com.jwtauth.entity.RefreshToken;
import com.jwtauth.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long>  {

    Optional<RefreshToken> findByToken(String token);
    RefreshToken findByUser(User user);

    @Modifying
    int deleteByUser(User user);
}
