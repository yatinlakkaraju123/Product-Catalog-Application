package com.yatindevhub.ecommerce.mappers.ProductCatalog;

import com.yatindevhub.ecommerce.dto.ProductCatalog.ProductRequestDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.ProductResponseDto;
import com.yatindevhub.ecommerce.entity.productCatalog.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMappers {

    Product productRequestToProduct(ProductRequestDto productRequestDto);
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponseDto productToProductResponse(Product product);
}
