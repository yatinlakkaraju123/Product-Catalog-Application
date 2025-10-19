package com.yatindevhub.ecommerce.mappers.ProductCatalog;

import com.yatindevhub.ecommerce.dto.ProductCatalog.ProductRequestDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.ProductResponseDto;
import com.yatindevhub.ecommerce.entity.productCatalog.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMappers {

    Product productRequestToProduct(ProductRequestDto productRequestDto);
    ProductResponseDto productToProductResponse(Product product);
}
