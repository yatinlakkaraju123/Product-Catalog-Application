package com.yatindevhub.ecommerce.controller;

import com.yatindevhub.ecommerce.exceptions.Cart.CartItemNotFoundException;
import com.yatindevhub.ecommerce.exceptions.Cart.CartNotFoundException;
import com.yatindevhub.ecommerce.exceptions.ProductCatalog.CategoryNotFoundException;
import com.yatindevhub.ecommerce.exceptions.ProductCatalog.ProductNotFoundException;
import com.yatindevhub.ecommerce.exceptions.Security.InvalidPasswordException;
import com.yatindevhub.ecommerce.exceptions.Security.UserAlreadyExistsException;
import com.yatindevhub.ecommerce.exceptions.Security.WrongCredentialsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex,HttpServletRequest httpServletRequest) {
        log.warn("Method argument not valid for request {}: {}",httpServletRequest.getRequestURI(),ex.toString());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Method argument not valid");
        problemDetail.setType(URI.create(httpServletRequest.getRequestURI()));
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);

    }

    // Optional: handle missing body / unreadable JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleUnreadable(HttpMessageNotReadableException ex,HttpServletRequest httpServletRequest) {
        log.warn("Http message not readable  for request {}: {}",httpServletRequest.getRequestURI(),ex.toString());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Http message not readable ");
        problemDetail.setType(URI.create(httpServletRequest.getRequestURI()));
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleInternalServerError(Exception e,HttpServletRequest httpServletRequest)
        {           log.warn("Internal Server Error for request {}: {}",httpServletRequest.getRequestURI(),e);

            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            problemDetail.setTitle("Internal Server Error");
            problemDetail.setType(URI.create(httpServletRequest.getRequestURI()));
            problemDetail.setDetail(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);




        }
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCategoryNotFound(CategoryNotFoundException categoryNotFoundException,
                                                                HttpServletRequest httpServletRequest)
    {   log.warn("Category Not Found for request: {}", categoryNotFoundException);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, categoryNotFoundException.getMessage());
        problemDetail.setTitle("Category Not Found");
        problemDetail.setType(URI.create(httpServletRequest.getRequestURI()));
        problemDetail.setDetail(categoryNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);



    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleProductNotFound(ProductNotFoundException productNotFoundException,
                                                                HttpServletRequest httpServletRequest)
    {
        log.warn("Access denied for request: {}",httpServletRequest.getRequestURI(), productNotFoundException);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, productNotFoundException.getMessage());
        problemDetail.setTitle("Product Not Found");
        problemDetail.setType(URI.create(httpServletRequest.getRequestURI()));
        problemDetail.setDetail(productNotFoundException.getMessage());

        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);


    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Access denied for request: {}", request.getRequestURI(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setTitle("Access Denied");
        problemDetail.setType(URI.create(request.getRequestURI()));
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problemDetail);
    }
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ProblemDetail> handleFileUploadException(FileUploadException fileUploadException,HttpServletRequest request){
        log.warn("File Upload error for request: {}", request.getRequestURI(), fileUploadException);
        fileUploadException.printStackTrace();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, fileUploadException.getMessage());
        problemDetail.setTitle("File Upload error");
        problemDetail.setType(URI.create(request.getRequestURI()));
        problemDetail.setDetail(fileUploadException.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCartItemNotFoundException(CartItemNotFoundException cartItemNotFoundException,HttpServletRequest request){
        log.warn("Cart Item not found error for request: {}", request.getRequestURI(), cartItemNotFoundException);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, cartItemNotFoundException.getMessage());
        problemDetail.setTitle("Cart Item Not found");
        problemDetail.setType(URI.create(request.getRequestURI()));
        problemDetail.setDetail(cartItemNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCartNotFoundException(CartNotFoundException cartNotFoundException,HttpServletRequest request){
        log.warn("Cart Not found error for request: {}", request.getRequestURI(), cartNotFoundException);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, cartNotFoundException.getMessage());
        problemDetail.setTitle("Cart not found");
        problemDetail.setType(URI.create(request.getRequestURI()));
        problemDetail.setDetail(cartNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ProblemDetail> optimisticLockingFailureException(OptimisticLockingFailureException optimisticLockingFailureException,HttpServletRequest request){
        log.warn("optimistic locking failure exception: {}", request.getRequestURI(), optimisticLockingFailureException);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, optimisticLockingFailureException.getMessage());
        problemDetail.setTitle("optimistic locking failure");
        problemDetail.setType(URI.create(request.getRequestURI()));
        problemDetail.setDetail(optimisticLockingFailureException.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> userAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException,HttpServletRequest request){
        log.warn("User already exists exception: {}", request.getRequestURI(), userAlreadyExistsException);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, userAlreadyExistsException.getMessage());
        problemDetail.setTitle("User already exists");
        problemDetail.setType(URI.create(request.getRequestURI()));
        problemDetail.setDetail(userAlreadyExistsException.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }
    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<ProblemDetail> wrongCredentialsException(WrongCredentialsException wrongCredentialsException,HttpServletRequest request){
        log.warn("Wrong Credentials exception: {}", request.getRequestURI(), wrongCredentialsException);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, wrongCredentialsException.getMessage());
        problemDetail.setTitle("Wrong Credentials");
        problemDetail.setType(URI.create(request.getRequestURI()));
        problemDetail.setDetail(wrongCredentialsException.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ProblemDetail> InvalidPasswordException(InvalidPasswordException invalidPasswordException,HttpServletRequest request){
        log.warn("Invalid password exception: {}", request.getRequestURI(), invalidPasswordException);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, invalidPasswordException.getMessage());
        problemDetail.setTitle("Invalid password");
        problemDetail.setType(URI.create(request.getRequestURI()));
        problemDetail.setDetail(invalidPasswordException.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }
}
