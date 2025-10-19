package com.yatindevhub.ecommerce.repository.Cart;

import com.yatindevhub.ecommerce.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {


}
