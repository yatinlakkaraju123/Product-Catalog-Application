package com.yatindevhub.ecommerce.service.ProductCatalog;

import com.yatindevhub.ecommerce.dto.PaginationDTO;
import com.yatindevhub.ecommerce.dto.PaginationRequest;
import com.yatindevhub.ecommerce.dto.ProductCatalog.CategoryRequestDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.CategoryResponseDto;
import com.yatindevhub.ecommerce.entity.productCatalog.Category;
import com.yatindevhub.ecommerce.exceptions.ProductCatalog.CategoryNotFoundException;
import com.yatindevhub.ecommerce.mappers.ProductCatalog.CategoryMappers;
import com.yatindevhub.ecommerce.repository.ProductCatalog.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest

@Sql(
        scripts = "/cleanup.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = "/data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMappers categoryMappers;


    @Test
    void getAllCategories_noCategoryAdded(){
        PaginationRequest paginationRequest = PaginationRequest.builder().page(0)
                .size(10).direction(Sort.Direction.ASC).sortField("id").build();
        PaginationDTO<CategoryResponseDto> paginationDTO = categoryService.getAllCategories(paginationRequest);

        Assertions.assertTrue(paginationDTO.isEmpty());
    }
    @Test
    void getAllCategories_categoryAdded(){
        PaginationRequest paginationRequest = PaginationRequest.builder().page(0)
                .size(10).direction(Sort.Direction.ASC).sortField("id").build();
        Category category = new Category();
        category.setName("test");
        categoryRepository.save(category);
        PaginationDTO<CategoryResponseDto> paginationDTO = categoryService.getAllCategories(paginationRequest);
        Assertions.assertTrue(paginationDTO.getContent().contains(categoryMappers.CategoryToCategoryResponse(category)));

    }
    @Test
    void getCategoryById_valid(){

        Category category = new Category();
        category.setName("testing");
       Category savedCategory =  categoryRepository.save(category);
        CategoryResponseDto categoryResponseDto = categoryService.getCategoryById(savedCategory.getId());
        Assertions.assertSame(categoryResponseDto.getName(), categoryMappers.CategoryToCategoryResponse(category).getName());
    }
    @Test
    void getCategoryById_inValid(){
        Assertions.assertThrows(CategoryNotFoundException.class,()->{
            categoryService.getCategoryById(10000);
        });
    }

    @Test
    void addCategory_valid(){
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("testingCategory");
       CategoryResponseDto categoryResponseDto =  categoryService.addCategory(categoryRequestDto);
       Assertions.assertSame(categoryRequestDto.getName(),categoryResponseDto.getName());

    }
    @Test
    void updateCategory_valid(){
        Category category = new Category();
        category.setName("testing Update");
        Category savedCategory =  categoryRepository.save(category);
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("testingCategory");
        CategoryResponseDto categoryResponseDto = categoryService.updateCategory(categoryRequestDto, savedCategory.getId());
        Assertions.assertSame(categoryResponseDto.getName(),categoryRequestDto.getName());
    }

    @Test
    void updateCategory_Invalid(){

        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("testingCategory");
        Assertions.assertThrows(CategoryNotFoundException.class,()->{
           categoryService.updateCategory(categoryRequestDto,10000);
        });
    }
    @Test
    void deleteCategory_valid(){
        Category category = new Category();
        category.setName("testing Update");
     Category category1 =     categoryRepository.save(category);
         Assertions.assertTrue(categoryService.deleteCategory(category1.getId()));
    }
    @Test
    void deleteCategory_inValid(){
      Assertions.assertThrows(CategoryNotFoundException.class,()->{
          categoryService.deleteCategory(10000);
      });
    }




}
