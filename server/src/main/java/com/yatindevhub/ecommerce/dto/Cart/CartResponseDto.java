package com.yatindevhub.ecommerce.dto.Cart;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class CartResponseDto {
    private long id;
    private String userId;
    private LocalDateTime expiryDate;
    private boolean isExpired;
    private long version;
}
