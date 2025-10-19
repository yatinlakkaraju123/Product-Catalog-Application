package com.yatindevhub.ecommerce.repository.security;

import com.yatindevhub.ecommerce.entity.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByTokenHash(String token);
    Optional<RefreshToken> findByTokenLookupKey(String tokenLookupKey);


}
