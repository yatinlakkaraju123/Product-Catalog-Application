package com.yatindevhub.ecommerce.controller.Cart;

import com.yatindevhub.ecommerce.dto.Cart.AddCartItemDto;
import com.yatindevhub.ecommerce.dto.Cart.CartItemResponseDto;
import com.yatindevhub.ecommerce.dto.Cart.CartResponseDto;
import com.yatindevhub.ecommerce.dto.Cart.ChangeQuantityDto;
import com.yatindevhub.ecommerce.service.Cart.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("cart/")
public class CartController {
    private final CartService cartService;

    @PostMapping("addItem")
    public ResponseEntity<CartResponseDto> addCartItem(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AddCartItemDto c){
        String username = userDetails.getUsername();
        return ResponseEntity.ok(cartService.addCartItem(username,c));


    }

    @PostMapping("changeQuantity")
    public ResponseEntity<CartItemResponseDto> changeQuantity(@RequestBody @Valid  ChangeQuantityDto changeQuantityDto){
        return ResponseEntity.ok(cartService.changeQuantity(changeQuantityDto));
    }

    @DeleteMapping("deleteCartItem")
    public ResponseEntity<Boolean> deleteCartItem(@RequestParam long cartItemId){
        return ResponseEntity.ok(cartService.deleteCartItem(cartItemId));
    }

    @DeleteMapping("deleteCart")
    public ResponseEntity<Boolean> deleteCart(@RequestParam long cartId){
        return ResponseEntity.ok(cartService.deleteCart(cartId));
    }


}
