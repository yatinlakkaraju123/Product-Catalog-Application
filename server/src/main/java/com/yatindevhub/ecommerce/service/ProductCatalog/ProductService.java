package com.yatindevhub.ecommerce.service.ProductCatalog;

import com.yatindevhub.ecommerce.config.S3Config;
import com.yatindevhub.ecommerce.dto.PaginationDTO;
import com.yatindevhub.ecommerce.dto.PaginationRequest;
import com.yatindevhub.ecommerce.dto.ProductCatalog.CategoryRequestDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.ProductRequestDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.ProductResponseDto;
import com.yatindevhub.ecommerce.entity.productCatalog.Category;
import com.yatindevhub.ecommerce.entity.productCatalog.Product;
import com.yatindevhub.ecommerce.exceptions.ProductCatalog.CategoryNotFoundException;
import com.yatindevhub.ecommerce.exceptions.ProductCatalog.ProductNotFoundException;
import com.yatindevhub.ecommerce.mappers.ProductCatalog.ProductMappers;
import com.yatindevhub.ecommerce.repository.ProductCatalog.CategoryRepository;
import com.yatindevhub.ecommerce.repository.ProductCatalog.ProductRepository;
import com.yatindevhub.ecommerce.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMappers productMappers;
    private final S3Config s3Config;
    public PaginationDTO<ProductResponseDto> getAllProducts(PaginationRequest paginationRequest){
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<Product> page = productRepository.findAll(pageable);
        List<ProductResponseDto> productResponseDtos =  page.getContent().
                stream().
                map(product -> {
                    ProductResponseDto productResponseDto = productMappers.productToProductResponse(product);
                    productResponseDto.setCategoryId(product.getCategory().getId());
                    productResponseDto.setCategoryName(product.getCategory().getName());
                    return productResponseDto;
                }).toList();
        return new PaginationDTO<>(productResponseDtos,
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.isEmpty()

                );
    }

    public ProductResponseDto getProductByProductId(long id){
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new ProductNotFoundException("there is no product with id:"+id);
        }
        ProductResponseDto productResponseDto = productMappers.productToProductResponse(product.get());
        productResponseDto.setCategoryId(product.get().getCategory().getId());
        productResponseDto.setCategoryName(product.get().getCategory().getName());
        return productResponseDto;

    }
    public PaginationDTO<ProductResponseDto> getProductsByCategory(long categoryId,PaginationRequest paginationRequest){
        log.info("Fetching category {}", categoryId);

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if(optionalCategory.isEmpty()) throw new CategoryNotFoundException("there is no category with this id:"+categoryId);
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<Product> products = productRepository.productsByCategory(categoryId,pageable);
        List<ProductResponseDto> productResponseDtos = products.getContent().stream().map(product -> {
                    ProductResponseDto productResponseDto = productMappers.productToProductResponse(product);
                    productResponseDto.setCategoryId(product.getCategory().getId());
                    productResponseDto.setCategoryName(product.getCategory().getName());
                    return productResponseDto;
                })
                .toList();
        return new PaginationDTO<ProductResponseDto>(
                productResponseDtos,
                products.getTotalPages(),
                products.getTotalElements(),
                products.getSize(),
                products.getNumber(),
                products.isEmpty()

        );


    }
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto, long categoryId, MultipartFile file) throws FileUploadException {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if(optionalCategory.isEmpty()) throw new CategoryNotFoundException("there is no category with this id:"+categoryId);
        Product product = productMappers.productRequestToProduct(productRequestDto);
        String imageUrl = "";
        if(file!=null &&
                !file.isEmpty())
        {
            imageUrl =  s3Config.uploadFile(file);
            product.setImageUrl(imageUrl);
        }

        product.setCategory(optionalCategory.get());
        return productMappers.productToProductResponse(productRepository.save(product));
    }

    public ProductResponseDto updateProduct(long id,long categoryId,ProductRequestDto productRequestDto,MultipartFile file) throws FileUploadException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));


        // Update fields only if they are not null in the DTO
        if (productRequestDto.getName() != null) {
            product.setName(productRequestDto.getName());
        }

        if (productRequestDto.getPrice() != null) {
            product.setPrice(productRequestDto.getPrice());
        }

        if (productRequestDto.getDescription() != null) {
            product.setDescription(productRequestDto.getDescription());
        }

        // Set the category
        product.setCategory(category);

        // Handle image update separately
        if (file != null && !file.isEmpty()) {
            String imageUrl = s3Config.uploadFile(file);
            product.setImageUrl(imageUrl);
        }
        return productMappers.productToProductResponse(productRepository.save(product));
    }

    public void deleteProduct(long id){
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()) throw new ProductNotFoundException("there is no product with id:"+id);
        productRepository.delete(product.get());
    }
}
