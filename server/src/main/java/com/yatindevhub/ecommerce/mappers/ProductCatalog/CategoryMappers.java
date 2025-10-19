package com.yatindevhub.ecommerce.mappers.ProductCatalog;

import com.yatindevhub.ecommerce.dto.ProductCatalog.CategoryRequestDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.CategoryResponseDto;
import com.yatindevhub.ecommerce.entity.productCatalog.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMappers {

    //    @Mapping(source = "categoryId",target = "id")
    Category CategoryRequestToCategory(CategoryRequestDto categoryRequestDto);
    CategoryResponseDto CategoryToCategoryResponse(Category category);
}
