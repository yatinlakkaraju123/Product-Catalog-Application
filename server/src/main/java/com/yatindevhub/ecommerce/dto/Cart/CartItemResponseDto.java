package com.yatindevhub.ecommerce.dto.Cart;

import lombok.Data;

@Data
public class CartItemResponseDto {
    private long id;
    private String productName;
    private String productPrice;
    private Integer quantity;
}
