package com.yatindevhub.ecommerce.controller.ProductCatalog;

import com.yatindevhub.ecommerce.config.S3Config;
import com.yatindevhub.ecommerce.dto.PaginationDTO;
import com.yatindevhub.ecommerce.dto.PaginationRequest;
import com.yatindevhub.ecommerce.dto.ProductCatalog.CategoryResponseDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.ProductRequestDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.ProductResponseDto;
import com.yatindevhub.ecommerce.service.ProductCatalog.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("products/")
public class ProductController {

    private final ProductService productService;
    private final S3Config s3Config;
    @GetMapping("")
    public ResponseEntity<PaginationDTO<ProductResponseDto>> getAllProducts(  @RequestParam(required = false) Integer page,
                                                                              @RequestParam(required = false) Integer size,
                                                                              @RequestParam(required = false) String sortField,
                                                                              @RequestParam(required = false) Sort.Direction direction){
        final PaginationRequest paginationRequest = new PaginationRequest(page, size, sortField, direction);

        return new ResponseEntity<>(productService.getAllProducts(paginationRequest), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable long id){
        return new ResponseEntity<>(productService.getProductByProductId(id),HttpStatus.OK);
    }
    @GetMapping("categories/{id}/products")
    public ResponseEntity<PaginationDTO<ProductResponseDto>> getProductsByCategory(@PathVariable long id,@RequestParam(required = false) Integer page,
                                                                                   @RequestParam(required = false) Integer size,
                                                                                   @RequestParam(required = false) String sortField,
                                                                                   @RequestParam(required = false) Sort.Direction direction){
        final PaginationRequest paginationRequest = new PaginationRequest(page, size, sortField, direction);

        return new ResponseEntity<>(productService.getProductsByCategory(id,paginationRequest),HttpStatus.OK);
    }
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> addProduct(@RequestPart("data") ProductRequestDto productRequestDto
            ,@RequestPart(name = "image",required = false) MultipartFile file) throws FileUploadException {
        long id = productRequestDto.getCategoryId();
        return new ResponseEntity<>(productService.addProduct(productRequestDto,id,file),HttpStatus.OK);
    }


    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable long id,
                                                            @RequestPart("data") ProductRequestDto productRequestDto,@RequestPart(name = "image",required = false) MultipartFile file) throws FileUploadException {
       long categoryId = productRequestDto.getCategoryId();
        return new ResponseEntity<>(productService.updateProduct(id,categoryId,productRequestDto,file),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable long id){
        productService.deleteProduct(id);
        return new ResponseEntity<>("product deleted successfully",HttpStatus.OK);
    }

    @PostMapping("/testUpload")
    public ResponseEntity<String> testFileUpload(@RequestParam("file") MultipartFile file) throws FileUploadException {

        return new ResponseEntity<>(s3Config.uploadFile(file),HttpStatus.OK);
    }
}
