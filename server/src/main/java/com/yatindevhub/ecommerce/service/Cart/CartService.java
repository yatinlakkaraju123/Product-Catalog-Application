package com.yatindevhub.ecommerce.service.Cart;

import com.yatindevhub.ecommerce.dto.Cart.AddCartItemDto;
import com.yatindevhub.ecommerce.dto.Cart.CartItemResponseDto;
import com.yatindevhub.ecommerce.dto.Cart.CartResponseDto;
import com.yatindevhub.ecommerce.dto.Cart.ChangeQuantityDto;
import com.yatindevhub.ecommerce.entity.cart.Cart;
import com.yatindevhub.ecommerce.entity.cart.CartItem;
import com.yatindevhub.ecommerce.entity.productCatalog.Product;
import com.yatindevhub.ecommerce.entity.security.UserInfo;
import com.yatindevhub.ecommerce.exceptions.Cart.CartItemNotFoundException;
import com.yatindevhub.ecommerce.exceptions.Cart.CartNotFoundException;
import com.yatindevhub.ecommerce.exceptions.ProductCatalog.ProductNotFoundException;
import com.yatindevhub.ecommerce.mappers.Cart.CartItemMapper;
import com.yatindevhub.ecommerce.mappers.Cart.CartMapper;
import com.yatindevhub.ecommerce.repository.Cart.CartItemRepository;
import com.yatindevhub.ecommerce.repository.Cart.CartRepository;
import com.yatindevhub.ecommerce.repository.ProductCatalog.ProductRepository;
import com.yatindevhub.ecommerce.repository.security.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    public Optional<Cart> isNotExpired(String userId){
       List<Cart> carts = cartRepository.allCartsByUserId(userId);
    return  carts.stream().filter(cart -> cart.getExpiryDate()!=null && LocalDateTime.now().isBefore(cart.getExpiryDate())).findFirst();

    }
    @Transactional
    public CartResponseDto addCartItem(String userName, AddCartItemDto addCartItemDto) {
        return retryableAddCartItem(userName, addCartItemDto);
    }

    @Retryable(
            value = OptimisticLockingFailureException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 100),
            exclude = ProductNotFoundException.class
    )
    public CartResponseDto retryableAddCartItem(String userName, AddCartItemDto addCartItemDto) {
        UserInfo userInfo = userRepository.findByUsername(userName);
        String userId = userInfo.getUserId();
        Optional<Cart> cartOptional = isNotExpired(userId);
        Cart savedCart = cartOptional.orElseGet(() ->
                cartRepository.save(
                        Cart.builder()
                                .expiryDate(LocalDateTime.now().plusHours(1))
                                .cartItems(new ArrayList<>())
                                .userId(userId)
                                .build()
                )
        );

        Optional<Product> product = productRepository.findById(addCartItemDto.getProductId());
        if (product.isEmpty()) {
            throw new ProductNotFoundException(
                    "the product with id:" + addCartItemDto.getProductId() + " is not available"
            );
        }

        Product prod = product.get();
        Optional<CartItem> existing = savedCart.getCartItems().stream()
                .filter(ci -> ci.getProductId() == addCartItemDto.getProductId())
                .findFirst();

        CartItem cartItem = existing.orElseGet(() ->
                CartItem.builder()
                        .cart(savedCart)
                        .productId(prod.getId())
                        .productName(prod.getName())
                        .productPrice(prod.getPrice().toPlainString())
                        .quantity(0)
                        .build()
        );

        cartItem.setQuantity(cartItem.getQuantity() + addCartItemDto.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        savedCart.getCartItems().add(savedCartItem);

        CartResponseDto dto = cartMapper.toDto(savedCart);
        dto.setExpired(savedCart.isExpired());
        return dto;
    }

    @Recover
    public CartResponseDto recover(OptimisticLockingFailureException e, String userName, AddCartItemDto dto) {
        log.error("Failed to update cart after retries for user {}: {}", userName, e.getMessage());
        throw e;
    }

    public CartItemResponseDto changeQuantity(ChangeQuantityDto changeQuantityDto){

        Optional<CartItem> optionalCartItem = cartItemRepository.findById(changeQuantityDto.getId());
        if(optionalCartItem.isPresent()){
            CartItem cartItem = optionalCartItem.get();
            cartItem.setQuantity(changeQuantityDto.getQuantity());
           return  cartItemMapper.toDto(cartItemRepository.save(cartItem));
        }else{
            throw new CartItemNotFoundException("the cart item for id:"+changeQuantityDto.getId()+"is not available");
        }

    }

    public boolean deleteCartItem(long cartItemId){
        boolean res = false;
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        if(optionalCartItem.isPresent()){
            CartItem cartItem = optionalCartItem.get();
            cartItemRepository.delete(cartItem);
            res = true;
            return res;
        }else{
            throw new CartItemNotFoundException("the cart item for id:"+cartItemId+"is not available");
        }

    }

    public boolean deleteCart(long cartId){
        boolean res = false;
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if(optionalCart.isPresent()){
            Cart cart = optionalCart.get();
            cartRepository.delete(cart);
            res = true;
            return res;
        }else{
            throw new CartNotFoundException("cart not found for id:"+cartId);
        }
    }




}
