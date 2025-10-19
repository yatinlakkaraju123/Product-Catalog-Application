package com.yatindevhub.ecommerce.exceptions.Cart;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(String message) {
        super(message);
    }
    public CartItemNotFoundException(String message,Throwable cause) {
        super(message,cause);
    }
}
