package com.yatindevhub.ecommerce.repository.Cart;

import com.yatindevhub.ecommerce.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query(nativeQuery = true,value = "select * from cart where user_id=? LIMIT 1")
    public List<Cart> allCartsByUserId(String userId);
}
