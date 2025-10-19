package com.yatindevhub.ecommerce.controller;

import com.yatindevhub.ecommerce.dto.security.*;
import com.yatindevhub.ecommerce.exceptions.Security.InvalidPasswordException;
import com.yatindevhub.ecommerce.exceptions.Security.UserAlreadyExistsException;
import com.yatindevhub.ecommerce.exceptions.Security.WrongCredentialsException;
import com.yatindevhub.ecommerce.repository.security.RefreshTokenRepository;
import com.yatindevhub.ecommerce.service.security.AuthenticationService;
import com.yatindevhub.ecommerce.service.security.JwtService;
import com.yatindevhub.ecommerce.service.security.RefreshTokenService;
import com.yatindevhub.ecommerce.service.security.UserServiceDetailsImpl;
import com.yatindevhub.ecommerce.utils.CookieUtils;
import com.yatindevhub.ecommerce.utils.ValidationUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Slf4j
public class AuthController {
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
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/auth/v1/signup")
    public ResponseEntity SignUp(@RequestBody UserInfoDto userInfoDto){
        if(!ValidationUtils.checkPassword(userInfoDto.getPassword())){
            throw new InvalidPasswordException("the provided password does not meet the complexity");
        }
            Boolean isSignedUp = userServiceDetails.signupUser(userInfoDto);
            if(Boolean.FALSE.equals(isSignedUp)){
                throw new UserAlreadyExistsException("User Already exists");

            }
            JwtResponseDTO jwtResponseDTO = authenticationService.signUp(userInfoDto);


            return ResponseEntity.ok()

                    .body(jwtResponseDTO);

    }


    @PostMapping("/auth/v1/login")
    public ResponseEntity  AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authRequestDTO.getUsername(),authRequestDTO.getPassword()
                ));
        if(authentication.isAuthenticated()){
              JwtResponseDTO jwtResponseDTO =   authenticationService.signIn(authRequestDTO);
            //ResponseCookie cookie = CookieUtils.createRefreshTokenCookie(jwtResponseDTO.getToken(),true);
            //AccessTokenDto accessTokenDto = new AccessTokenDto(jwtResponseDTO.getAccessToken());

            return ResponseEntity.ok()

                    .body(jwtResponseDTO);
        }
        else{
            throw new WrongCredentialsException("wrong credentials");
        }
    }


    @PostMapping("/auth/v1/refreshToken")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody  RefreshTokenRequest refreshTokenRequest,
                                                       HttpServletRequest request) {


        String refreshToken = refreshTokenRequest.getToken();;
        if (refreshToken == null || refreshToken.isEmpty()) {
            log.warn("Refresh token missing in cookie");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Get new tokens from refresh token
        JwtResponseDTO jwtResponseDTO = authenticationService.refreshToken(refreshToken);

//        // Update refresh token cookie
//        ResponseCookie cookie = CookieUtils.createRefreshTokenCookie(
//                jwtResponseDTO.getToken(),
//                true
//        );
//
//        // CRITICAL FIX: Return AccessTokenDto, not JwtResponseDTO
//        AccessTokenDto accessTokenDto = new AccessTokenDto(jwtResponseDTO.getAccessToken());

        log.info("Successfully refreshed token");

        return ResponseEntity.ok()

                .body(jwtResponseDTO);

    }

    @PostMapping("/auth/v1/logout")
    public ResponseEntity logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getToken();
        log.info("reached logout controller");
        if (refreshToken != null) {
           refreshTokenService.deleteByToken(refreshToken);
        }


        return ResponseEntity.ok()

                .build();
    }


}
