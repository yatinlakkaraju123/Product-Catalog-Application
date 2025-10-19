package com.yatindevhub.ecommerce.service.security;

import com.yatindevhub.ecommerce.entity.security.RefreshToken;
import com.yatindevhub.ecommerce.entity.security.UserInfo;
import com.yatindevhub.ecommerce.repository.security.RefreshTokenRepository;
import com.yatindevhub.ecommerce.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final long REFRESH_TOKEN_VALIDITY = 15L * 24 * 60 * 60 * 1000; // 7 days in milliseconds

    public String createRefreshToken(String username){
        UserInfo userInfoExtracted = userRepository.findByUsername(username);
        String token = UUID.randomUUID().toString();
        Set<UserInfo> userInfos = new HashSet<>();
        userInfos.add(userInfoExtracted);
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfoExtracted)
                .tokenHash(passwordEncoder.encode(token))
                .tokenLookupKey(computeLookupKey(token))
                .expiryDate(Instant.now().plusSeconds(86400*15))
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }
    public void deleteByToken(String token) {
        refreshTokenRepository.findByTokenHash(token)
                .ifPresent(refreshTokenRepository::delete);
    }
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            // Token is expired - delete it and throw exception
            refreshTokenRepository.delete(token);
            log.warn("Refresh token expired for user: {}", token.getUserInfo().getUsername());
            throw new RuntimeException("Refresh token expired. Please login again.");
        }

        // Token is valid - optionally extend it
        token.setExpiryDate(Instant.now().plusMillis(REFRESH_TOKEN_VALIDITY));
        refreshTokenRepository.save(token);

        log.debug("Refresh token verified and extended for user: {}", token.getUserInfo().getUsername());
        return token;
    }

    public Optional<RefreshToken> findByToken(String plainToken) {
        String lookupKey = computeLookupKey(plainToken);
        return refreshTokenRepository.findByTokenLookupKey(lookupKey)
                .filter(rt -> passwordEncoder.matches(plainToken, rt.getTokenHash()));
    }



    public void revokeAllTokensForUser(String userId) {
        List<RefreshToken> refreshTokens = refreshTokenRepository.findAll();
        refreshTokens.stream()
                .filter(rt -> rt.getUserInfo().getUserId().equals(userId))
                .forEach(rt -> {
                    rt.setRevoked(true);
                    refreshTokenRepository.save(rt);
                });
    }

    private String computeLookupKey(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

}

