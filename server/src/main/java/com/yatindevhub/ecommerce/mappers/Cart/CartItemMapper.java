package com.yatindevhub.ecommerce.mappers.Cart;

import com.yatindevhub.ecommerce.dto.Cart.CartItemResponseDto;
import com.yatindevhub.ecommerce.entity.cart.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemResponseDto toDto(CartItem cartItem);
}
