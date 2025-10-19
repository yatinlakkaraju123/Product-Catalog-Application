package com.yatindevhub.ecommerce.service.security;

import com.yatindevhub.ecommerce.dto.security.AuthRequestDTO;
import com.yatindevhub.ecommerce.dto.security.JwtResponseDTO;
import com.yatindevhub.ecommerce.dto.security.RefreshTokenRequest;
import com.yatindevhub.ecommerce.dto.security.UserInfoDto;
import com.yatindevhub.ecommerce.entity.security.RefreshToken;
import com.yatindevhub.ecommerce.exceptions.Security.InvalidPasswordException;
import com.yatindevhub.ecommerce.repository.security.RefreshTokenRepository;
import com.yatindevhub.ecommerce.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class AuthenticationService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserServiceDetailsImpl userServiceDetails;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    public JwtResponseDTO
    signUp(UserInfoDto userInfoDto){

        String refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
        String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());
        return JwtResponseDTO.builder().accessToken(jwtToken)
                .token(refreshToken).build();
    }


    public JwtResponseDTO signIn(AuthRequestDTO authRequestDTO){
        String refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
        return JwtResponseDTO.builder()
                .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
                .token(refreshToken)
                .build();
    }

    public JwtResponseDTO refreshToken(String refreshToken) {
        log.debug("Processing refresh token request");

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)  // This should throw if expired
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    // FIXED: Use consistent method name (check your JwtService)
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());

                    log.debug("Generated new access token for user: {}", userInfo.getUsername());

                    // Return same refresh token (no rotation)
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshToken)  // Reuse same token
                            .build();
                })
                .orElseThrow(() -> {
                    log.warn("Refresh token not found in database");
                    return new RuntimeException("Invalid refresh token");
                });
    }

    public void signOut(String refreshToken){

        RefreshToken storedToken = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not in db"));
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);
    }
}
