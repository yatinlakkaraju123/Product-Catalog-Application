package com.yatindevhub.ecommerce.service.ProductCatalog;

import com.yatindevhub.ecommerce.dto.PaginationDTO;
import com.yatindevhub.ecommerce.dto.PaginationRequest;
import com.yatindevhub.ecommerce.dto.ProductCatalog.CategoryRequestDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.CategoryResponseDto;
import com.yatindevhub.ecommerce.entity.productCatalog.Category;
import com.yatindevhub.ecommerce.entity.productCatalog.Product;
import com.yatindevhub.ecommerce.exceptions.ProductCatalog.CategoryNotFoundException;
import com.yatindevhub.ecommerce.mappers.ProductCatalog.CategoryMappers;
import com.yatindevhub.ecommerce.repository.ProductCatalog.CategoryRepository;
import com.yatindevhub.ecommerce.repository.ProductCatalog.ProductRepository;
import com.yatindevhub.ecommerce.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMappers categoryMappers;
    private final ProductRepository productRepository;
    private static final  String NO_CATEGORY_FOUND_EXCEPTION = "there is no category with id:";
    public PaginationDTO<CategoryResponseDto> getAllCategories(PaginationRequest paginationRequest){
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<Category> page = categoryRepository.findAll(pageable);
        List<CategoryResponseDto> categoryResponseDtos = page.getContent().stream().map(categoryMappers::CategoryToCategoryResponse)
                .toList();
        return  new PaginationDTO<>(
                categoryResponseDtos,
                page.getTotalPages(),

                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.isEmpty()


        );
    }

    public CategoryResponseDto getCategoryById(long id){
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) throw new CategoryNotFoundException(NO_CATEGORY_FOUND_EXCEPTION + id);
        return categoryMappers.CategoryToCategoryResponse(category.get());

    }

    public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto){
           Category category = categoryMappers.CategoryRequestToCategory(categoryRequestDto);
           Category category1 = categoryRepository.save(category);
           return categoryMappers.CategoryToCategoryResponse(category1);
    }

    public CategoryResponseDto updateCategory(CategoryRequestDto categoryRequestDto,long id){
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()){
            Category requestCategory = categoryMappers.CategoryRequestToCategory(categoryRequestDto);
            if(requestCategory.getName()!=null) category.get().setName(requestCategory.getName());
            return categoryMappers.CategoryToCategoryResponse(categoryRepository.save(category.get()));
    }
        else{
            throw new CategoryNotFoundException(NO_CATEGORY_FOUND_EXCEPTION + id);
        }
   }
    @Transactional
    public boolean deleteCategory(long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(NO_CATEGORY_FOUND_EXCEPTION + id);
        }

        // Products first
        productRepository.deleteByCategoryId(id);

        // Category after
        categoryRepository.deleteById(id);

        return true;
    }





    public void deleteAllCategories(){
        categoryRepository.deleteAll();
   }
}
