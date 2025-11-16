package com.yatindevhub.ecommerce.service.ProductCatalog;

import com.yatindevhub.ecommerce.dto.PaginationRequest;
import com.yatindevhub.ecommerce.dto.ProductCatalog.ProductRequestDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.ProductResponseDto;
import com.yatindevhub.ecommerce.entity.productCatalog.Category;
import com.yatindevhub.ecommerce.entity.productCatalog.Product;
import com.yatindevhub.ecommerce.exceptions.ProductCatalog.CategoryNotFoundException;
import com.yatindevhub.ecommerce.exceptions.ProductCatalog.ProductNotFoundException;
import com.yatindevhub.ecommerce.repository.ProductCatalog.CategoryRepository;
import com.yatindevhub.ecommerce.repository.ProductCatalog.ProductRepository;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

@SpringBootTest

@Sql(
        scripts = "/cleanup.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = "/data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class ProductServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void getAllProducts_whenEmpty(){
        PaginationRequest paginationRequest = PaginationRequest.builder().page(0)
                .size(10).direction(Sort.Direction.ASC).sortField("id").build();
        Assertions.assertTrue(productService.getAllProducts(paginationRequest).getContent().isEmpty());
    }

    @Test
    void getAllProducts_whenNotEmpty(){
        PaginationRequest paginationRequest = PaginationRequest.builder().page(0)
                .size(10).direction(Sort.Direction.ASC).sortField("id").build();
        Category category = new Category();
        category.setName("test");
       Category savedCategory =  categoryRepository.save(category);
        Product product = new Product();
        product.setName("test product");
        product.setDescription("it is a good product");
        product.setPrice(BigDecimal.valueOf(100));
        product.setCategory(savedCategory);
        productRepository.save(product);
        Assertions.assertTrue(productService.getAllProducts(paginationRequest).
                getContent().stream().
                anyMatch(productResponseDto -> productResponseDto.getName().equals(product.getName()) ));
    }
    @Test
    void getProductByProductId_whenInValid(){
     Assertions.assertThrows(ProductNotFoundException.class,()->{
         productService.getProductByProductId(1000);
     });
    }

    @Test
    void getProductByProductId_whenValid(){

        Category category = new Category();
        category.setName("test");
        Category savedCategory =  categoryRepository.save(category);
        Product product = new Product();
        product.setName("test product");
        product.setDescription("it is a good product");
        product.setPrice(BigDecimal.valueOf(100));
        product.setCategory(savedCategory);
       Product savedProduct =  productRepository.save(product);
        Assertions.assertEquals(productService.getProductByProductId(savedProduct.getId()).getName(), savedProduct.getName());
        Assertions.assertEquals(productService.getProductByProductId(savedProduct.getId()).getDescription(), savedProduct.getDescription());
        Assertions.assertEquals(productService.getProductByProductId(savedProduct.getId()).getPrice().intValue(), savedProduct.getPrice().intValue());
        Assertions.assertEquals(productService.getProductByProductId(savedProduct.getId()).getCategoryId(), savedProduct.getCategory().getId());

    }
    @Test
    void getProductByCategory_whenValid(){
        PaginationRequest paginationRequest = PaginationRequest.builder().page(0)
                .size(10).direction(Sort.Direction.ASC).sortField("id").build();
        Category category = new Category();
        category.setName("test");
        Category savedCategory =  categoryRepository.save(category);
        Product product = new Product();
        product.setName("test product");
        product.setDescription("it is a good product");
        product.setPrice(BigDecimal.valueOf(100));
        product.setCategory(savedCategory);
        Product savedProduct =  productRepository.save(product);
        Assertions.assertTrue(productService.getProductsByCategory(savedCategory.getId(), paginationRequest)
                .getContent().stream().anyMatch(productResponseDto ->
                        productResponseDto.getName().equals(savedProduct.getName())));
        Assertions.assertTrue(productService.getProductsByCategory(savedCategory.getId(), paginationRequest)
                .getContent().stream().anyMatch(productResponseDto ->
                        productResponseDto.getDescription().equals(savedProduct.getDescription())));
        Assertions.assertTrue(productService.getProductsByCategory(savedCategory.getId(), paginationRequest)
                .getContent().stream().anyMatch(productResponseDto ->
                        productResponseDto.getPrice().intValue()==savedProduct.getPrice().intValue()));
        Assertions.assertTrue(productService.getProductsByCategory(savedCategory.getId(), paginationRequest)
                .getContent().stream().anyMatch(productResponseDto ->
                        productResponseDto.getCategoryName().equals(savedProduct.getCategory().getName())));

    }
    @Test
    void getProductsByCategory_whenInValid(){
        Assertions.assertThrows(CategoryNotFoundException.class,()->{
            PaginationRequest paginationRequest = PaginationRequest.builder().page(0)
                    .size(10).direction(Sort.Direction.ASC).sortField("id").build();
            productService.getProductsByCategory(10000,paginationRequest);
        });
    }
    @Test
    void addProduct_valid() throws FileUploadException {
        Category category = new Category();
        category.setName("test");
        Category savedCategory = categoryRepository.saveAndFlush(category);
        Assertions.assertTrue(savedCategory.getId() > 0);

        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("test product");
        productRequestDto.setDescription("good product");
        productRequestDto.setPrice(BigDecimal.valueOf(100));
        productRequestDto.setCategoryId(savedCategory.getId());

        ProductResponseDto response = productService.addProduct(productRequestDto, savedCategory.getId(), null);

        Assertions.assertEquals(productRequestDto.getName(), response.getName());
        Assertions.assertEquals(productRequestDto.getDescription(), response.getDescription());
        Assertions.assertEquals(productRequestDto.getPrice().intValue(), response.getPrice().intValue());
        Assertions.assertEquals(savedCategory.getId(), response.getCategoryId());
    }

    @Test
    void addProduct_whenInValid() {
        Assertions.assertThrows(CategoryNotFoundException.class,()->{
            ProductRequestDto productRequestDto = new ProductRequestDto();
            productRequestDto.setName("test product");
            productRequestDto.setDescription("good product");
            productRequestDto.setPrice(BigDecimal.valueOf(100));
            productRequestDto.setCategoryId(1000);
            productService.addProduct(productRequestDto,1000,null);
        });
    }
    @Test
    void updateProduct_whenValid() throws FileUploadException {
        Category category = new Category();
        category.setName("test");
        Category savedCategory = categoryRepository.saveAndFlush(category);
        Assertions.assertTrue(savedCategory.getId() > 0);

        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("test product");
        productRequestDto.setDescription("good product");
        productRequestDto.setPrice(BigDecimal.valueOf(100));
        productRequestDto.setCategoryId(savedCategory.getId());

       ProductResponseDto savedProduct =   productService.addProduct(productRequestDto, savedCategory.getId(), null);
        Category category1 = new Category();
        category1.setName("test");
        Category savedCategory1 = categoryRepository.saveAndFlush(category1);
        Assertions.assertTrue(savedCategory1.getId() > 0);

        ProductRequestDto productRequestDto1 = new ProductRequestDto();
        productRequestDto1.setName("test product 2");
        productRequestDto1.setDescription("good product 2");
        productRequestDto1.setPrice(BigDecimal.valueOf(1000));
        productRequestDto1.setCategoryId(savedCategory1.getId());
        ProductResponseDto response = productService.updateProduct(savedProduct.getId(),savedCategory1.getId(),productRequestDto1 , null);

        Assertions.assertEquals(productRequestDto1.getName(), response.getName());
        Assertions.assertEquals(productRequestDto1.getDescription(), response.getDescription());
        Assertions.assertEquals(productRequestDto1.getPrice().intValue(), response.getPrice().intValue());

        Assertions.assertEquals(savedCategory1.getId(), response.getCategoryId());

    }
    @Test
    void updateProduct_whenInValid(){
        Assertions.assertThrows(CategoryNotFoundException.class,()->{
            Category category = new Category();
            category.setName("test");
            Category savedCategory = categoryRepository.saveAndFlush(category);
            Assertions.assertTrue(savedCategory.getId() > 0);

            ProductRequestDto productRequestDto = new ProductRequestDto();
            productRequestDto.setName("test product");
            productRequestDto.setDescription("good product");
            productRequestDto.setPrice(BigDecimal.valueOf(100));
            productRequestDto.setCategoryId(savedCategory.getId());

            ProductResponseDto savedProduct =   productService.addProduct(productRequestDto, savedCategory.getId(), null);
            ProductRequestDto productRequestDto1 = new ProductRequestDto();
            productRequestDto1.setName("test product");
            productRequestDto1.setDescription("good product");
            productRequestDto1.setPrice(BigDecimal.valueOf(100));
            productRequestDto1.setCategoryId(savedCategory.getId());
            productService.updateProduct(savedProduct.getId(), 10000,productRequestDto1,null);
        });
        Assertions.assertThrows(CategoryNotFoundException.class,()->{
            Category category = new Category();
            category.setName("test");
            Category savedCategory = categoryRepository.saveAndFlush(category);
            Assertions.assertTrue(savedCategory.getId() > 0);



            ProductRequestDto productRequestDto1 = new ProductRequestDto();
            productRequestDto1.setName("test product");
            productRequestDto1.setDescription("good product");
            productRequestDto1.setPrice(BigDecimal.valueOf(100));
            productRequestDto1.setCategoryId(savedCategory.getId());
            productService.updateProduct(1000, 10000,productRequestDto1,null);
        });
    }
    @Test
    void deleteProduct_whenInValid()  {
       Assertions.assertThrows(ProductNotFoundException.class,()->{
           productService.deleteProduct(1000);
       });

    }



}
