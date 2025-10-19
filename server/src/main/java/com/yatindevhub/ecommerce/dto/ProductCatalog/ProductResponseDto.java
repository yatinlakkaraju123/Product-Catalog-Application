package com.yatindevhub.ecommerce.dto.ProductCatalog;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDto {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private long categoryId;
    private String categoryName;
}
