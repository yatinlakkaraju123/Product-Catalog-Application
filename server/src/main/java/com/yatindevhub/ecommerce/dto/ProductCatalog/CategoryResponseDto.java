package com.yatindevhub.ecommerce.dto.ProductCatalog;

import com.yatindevhub.ecommerce.entity.productCatalog.Product;
import lombok.Data;

import java.util.Set;

@Data
public class CategoryResponseDto {
    private long id;
    private String name;
}
