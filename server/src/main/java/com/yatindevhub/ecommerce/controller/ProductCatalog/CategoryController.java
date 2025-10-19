package com.yatindevhub.ecommerce.controller.ProductCatalog;

import com.yatindevhub.ecommerce.dto.PaginationDTO;
import com.yatindevhub.ecommerce.dto.PaginationRequest;
import com.yatindevhub.ecommerce.dto.ProductCatalog.CategoryRequestDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.CategoryResponseDto;
import com.yatindevhub.ecommerce.entity.productCatalog.Category;
import com.yatindevhub.ecommerce.exceptions.ProductCatalog.CategoryNotFoundException;
import com.yatindevhub.ecommerce.service.ProductCatalog.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category/")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("")
    public ResponseEntity<PaginationDTO<CategoryResponseDto>> getAllCategories(@RequestParam(required = false) Integer page,
                                                                               @RequestParam(required = false) Integer size,
                                                                               @RequestParam(required = false) String sortField,
                                                                               @RequestParam(required = false) Sort.Direction direction){
        final PaginationRequest paginationRequest = new PaginationRequest(page, size, sortField, direction);

                return new ResponseEntity<>(categoryService.getAllCategories(paginationRequest), HttpStatus.OK);

    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable long id){

            CategoryResponseDto category = categoryService.getCategoryById(id);
             return new ResponseEntity<>(category,HttpStatus.OK);



    }
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDto> insertCategory(@RequestBody CategoryRequestDto categoryRequestDto){

                CategoryResponseDto categoryResponseDto = categoryService.addCategory(categoryRequestDto);
                return new ResponseEntity<>(categoryResponseDto,HttpStatus.OK);

    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDto> updateCategory(@RequestBody CategoryRequestDto categoryRequestDto,@PathVariable long id){

                    CategoryResponseDto categoryResponseDto = categoryService.updateCategory(categoryRequestDto,id);
                    return new ResponseEntity<>(categoryResponseDto,HttpStatus.OK);


    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){
            categoryService.deleteCategory(id);
            return new ResponseEntity<>("category deleted",HttpStatus.OK);


    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAllCategories(){

            categoryService.deleteAllCategories();
            return new ResponseEntity<>("deleted all categories",HttpStatus.OK);

    }



}
