package com.yatindevhub.ecommerce.utils;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtils {


    public static ResponseCookie createRefreshTokenCookie(String token,boolean secure){
        return ResponseCookie.from("refreshToken",token).
                httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite("None")
                .maxAge(Duration.ofDays(15)).
                build();
    }

    public static ResponseCookie clearRefreshTokenCookie(boolean secure) {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite("None")
                .maxAge(0) // delete immediately
                .build();
    }
}
