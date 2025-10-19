package com.yatindevhub.ecommerce.mappers.Cart;

import com.yatindevhub.ecommerce.dto.Cart.CartResponseDto;
import com.yatindevhub.ecommerce.entity.cart.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartResponseDto toDto(Cart cart);
}
