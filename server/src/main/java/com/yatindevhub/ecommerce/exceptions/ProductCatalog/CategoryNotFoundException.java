package com.yatindevhub.ecommerce.exceptions.ProductCatalog;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(String message,Throwable cause){
      super(message, cause);
    }
}
