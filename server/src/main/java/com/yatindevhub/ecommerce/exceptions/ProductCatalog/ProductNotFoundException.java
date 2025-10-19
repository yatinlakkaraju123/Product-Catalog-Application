package com.yatindevhub.ecommerce.exceptions.ProductCatalog;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
    public ProductNotFoundException(String message,Throwable cause){
        super(message, cause);
    }
}
