package com.yatindevhub.ecommerce.repository.ProductCatalog;

import com.yatindevhub.ecommerce.entity.productCatalog.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query(nativeQuery = true,value = "select * from products where category_id=?"
            ,countQuery = "select count(*) from products where category_id=?")
    Page<Product> productsByCategory(long categoryId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from Product p where p.category.id = :categoryId")
    void deleteByCategoryId(@Param("categoryId") Long categoryId);
}
