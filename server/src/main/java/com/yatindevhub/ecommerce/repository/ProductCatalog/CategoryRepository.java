package com.yatindevhub.ecommerce.repository.ProductCatalog;

import com.yatindevhub.ecommerce.entity.productCatalog.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
