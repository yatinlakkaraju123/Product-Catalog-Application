package com.yatindevhub.ecommerce.exceptions.Security;

public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException(String message) {
        super(message);
    }
    public WrongCredentialsException(String message,Throwable cause) {
        super(message);
    }
}
