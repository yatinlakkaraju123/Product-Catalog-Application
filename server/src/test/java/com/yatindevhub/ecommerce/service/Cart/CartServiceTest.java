package com.yatindevhub.ecommerce.service.Cart;

import com.yatindevhub.ecommerce.dto.Cart.AddCartItemDto;
import com.yatindevhub.ecommerce.dto.Cart.CartItemResponseDto;
import com.yatindevhub.ecommerce.dto.Cart.CartResponseDto;
import com.yatindevhub.ecommerce.dto.Cart.ChangeQuantityDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.CategoryRequestDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.CategoryResponseDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.ProductRequestDto;
import com.yatindevhub.ecommerce.dto.ProductCatalog.ProductResponseDto;
import com.yatindevhub.ecommerce.dto.security.UserInfoDto;
import com.yatindevhub.ecommerce.entity.cart.Cart;
import com.yatindevhub.ecommerce.entity.cart.CartItem;
import com.yatindevhub.ecommerce.entity.security.UserInfo;
import com.yatindevhub.ecommerce.exceptions.Cart.CartItemNotFoundException;
import com.yatindevhub.ecommerce.exceptions.Cart.CartNotFoundException;
import com.yatindevhub.ecommerce.exceptions.ProductCatalog.ProductNotFoundException;
import com.yatindevhub.ecommerce.repository.Cart.CartItemRepository;
import com.yatindevhub.ecommerce.repository.Cart.CartRepository;
import com.yatindevhub.ecommerce.repository.security.UserRepository;
import com.yatindevhub.ecommerce.service.ProductCatalog.CategoryService;
import com.yatindevhub.ecommerce.service.ProductCatalog.ProductService;
import com.yatindevhub.ecommerce.service.security.UserServiceDetailsImpl;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
@Sql(
        scripts = "/cleanup.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = "/data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class CartServiceTest {

    @MockitoSpyBean
    private CartService cartService;
    @Autowired
    private UserServiceDetailsImpl userServiceDetails;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;


    @Test
    void addCartItemTest_NewCartItem() throws FileUploadException {
        UserInfoDto userInfoDto = new UserInfoDto("test",9885298032L,"test@yopmail.com");
        userInfoDto.setUsername("test");
        userInfoDto.setPassword("123");

        userServiceDetails.signupUser(userInfoDto);
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("test");
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(categoryRequestDto);
        ProductRequestDto productRequestDto = new ProductRequestDto("test","test product",BigDecimal.ONE,categoryResponseDto.getId());
        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto,categoryResponseDto.getId(),null);
        AddCartItemDto addCartItemDto = new AddCartItemDto(productResponseDto.getId(), 12);
        CartResponseDto cartResponseDto = cartService.addCartItem("test",addCartItemDto);
        Assertions.assertFalse(cartResponseDto.isExpired());
        Assertions.assertTrue(cartResponseDto.getId()!=0L);
        Assertions.assertFalse(cartResponseDto.getUserId() == null || cartResponseDto.getUserId().isEmpty() );

    }
    @Test
    void addCartItemTest_ExistingCart() throws FileUploadException {
        UserInfoDto userInfoDto = new UserInfoDto("test",9885298032L,"test@yopmail.com");
        userInfoDto.setUsername("test");
        userInfoDto.setPassword("123");

        userServiceDetails.signupUser(userInfoDto);
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("test");
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(categoryRequestDto);
        ProductRequestDto productRequestDto = new ProductRequestDto("test","test product",BigDecimal.ONE,categoryResponseDto.getId());
        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto,categoryResponseDto.getId(),null);
        UserInfo userInfo = userRepository.findByUsername("test");
        Cart cart = Cart.builder().expiryDate(LocalDateTime.now().plusHours(1)).
                cartItems(new ArrayList<>()).userId(userInfo.getUserId())

                .build();
      cartRepository.save(cart);
        AddCartItemDto addCartItemDto = new AddCartItemDto(productResponseDto.getId(), 12);
        CartResponseDto cartResponseDto = cartService.addCartItem("test",addCartItemDto);
        Assertions.assertFalse(cartResponseDto.isExpired());
        Assertions.assertTrue(cartResponseDto.getId()!=0L);
        Assertions.assertFalse(cartResponseDto.getUserId() == null || cartResponseDto.getUserId().isEmpty() );

    }
    @Test
    void addCartItemTest_InvalidProduct()  {
        UserInfoDto userInfoDto = new UserInfoDto("test",9885298032L,"test@yopmail.com");
        userInfoDto.setUsername("test");
        userInfoDto.setPassword("123");

        userServiceDetails.signupUser(userInfoDto);

        AddCartItemDto addCartItemDto = new AddCartItemDto(11L, 12);

       Assertions.assertThrows(ProductNotFoundException.class,()->{
           CartResponseDto cartResponseDto = cartService.addCartItem("test",addCartItemDto);
       });

    }
    @Test
    void addCartItemTest_InvalidQuantity() throws FileUploadException {
        UserInfoDto userInfoDto = new UserInfoDto("test",9885298032L,"test@yopmail.com");
        userInfoDto.setUsername("test");
        userInfoDto.setPassword("123");

        userServiceDetails.signupUser(userInfoDto);
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("test");
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(categoryRequestDto);
        ProductRequestDto productRequestDto = new ProductRequestDto("test","test product",BigDecimal.ONE,categoryResponseDto.getId());
        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto,categoryResponseDto.getId(),null);
        AddCartItemDto addCartItemDto = new AddCartItemDto(productResponseDto.getId(), -1);
        CartResponseDto cartResponseDto = cartService.addCartItem("test",addCartItemDto);
        Assertions.assertFalse(cartResponseDto.isExpired());
        Assertions.assertTrue(cartResponseDto.getId()!=0L);
        Assertions.assertFalse(cartResponseDto.getUserId() == null || cartResponseDto.getUserId().isEmpty() );

    }

    @Test
    void changeQuantitytest_Valid() throws FileUploadException {
        UserInfoDto userInfoDto = new UserInfoDto("test",9885298032L,"test@yopmail.com");
        userInfoDto.setUsername("test");
        userInfoDto.setPassword("123");

        userServiceDetails.signupUser(userInfoDto);
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("test");
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(categoryRequestDto);
        ProductRequestDto productRequestDto = new ProductRequestDto("test","test product",BigDecimal.ONE,categoryResponseDto.getId());
        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto,categoryResponseDto.getId(),null);
        AddCartItemDto addCartItemDto = new AddCartItemDto(productResponseDto.getId(), 12);
        CartResponseDto cartResponseDto = cartService.addCartItem("test",addCartItemDto);
        CartItem cartItem = cartItemRepository.findAll().get(0);
        ChangeQuantityDto changeQuantityDto = new ChangeQuantityDto(cartItem.getId(),2);
        CartItemResponseDto cartItemResponseDto = cartService.changeQuantity(changeQuantityDto);
        Assertions.assertNotEquals(0L, cartItemResponseDto.getId());
        Assertions.assertFalse(cartItemResponseDto.getProductName()==null || cartItemResponseDto.getProductName().isEmpty());
        Assertions.assertFalse(cartItemResponseDto.getProductPrice()==null || cartItemResponseDto.getProductPrice().isEmpty());
        Assertions.assertEquals(2,cartItemResponseDto.getQuantity());

    }
    @Test
    void changeQuantitytest_Invalid() throws FileUploadException {
        UserInfoDto userInfoDto = new UserInfoDto("test",9885298032L,"test@yopmail.com");
        userInfoDto.setUsername("test");
        userInfoDto.setPassword("123");

        userServiceDetails.signupUser(userInfoDto);
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("test");
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(categoryRequestDto);
        ProductRequestDto productRequestDto = new ProductRequestDto("test","test product",BigDecimal.ONE,categoryResponseDto.getId());
        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto,categoryResponseDto.getId(),null);
        AddCartItemDto addCartItemDto = new AddCartItemDto(productResponseDto.getId(), 12);
        CartResponseDto cartResponseDto = cartService.addCartItem("test",addCartItemDto);
        CartItem cartItem = cartItemRepository.findAll().get(0);
        ChangeQuantityDto changeQuantityDto = new ChangeQuantityDto(123,2);

        Assertions.assertThrows(CartItemNotFoundException.class,()->{
            CartItemResponseDto cartItemResponseDto = cartService.changeQuantity(changeQuantityDto);
        });


    }

    @Test
    void deleteCartItemTest_valid() throws FileUploadException {
        UserInfoDto userInfoDto = new UserInfoDto("test",9885298032L,"test@yopmail.com");
        userInfoDto.setUsername("test");
        userInfoDto.setPassword("123");

        userServiceDetails.signupUser(userInfoDto);
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("test");
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(categoryRequestDto);
        ProductRequestDto productRequestDto = new ProductRequestDto("test","test product",BigDecimal.ONE,categoryResponseDto.getId());
        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto,categoryResponseDto.getId(),null);
        AddCartItemDto addCartItemDto = new AddCartItemDto(productResponseDto.getId(), 12);
        CartResponseDto cartResponseDto = cartService.addCartItem("test",addCartItemDto);
        CartItem cartItem = cartItemRepository.findAll().get(0);
        boolean isDeleted = cartService.deleteCartItem(cartItem.getId());
        Assertions.assertTrue(isDeleted);
    }
    @Test
    void deleteCartItemTest_Invalid() throws FileUploadException {
        UserInfoDto userInfoDto = new UserInfoDto("test",9885298032L,"test@yopmail.com");
        userInfoDto.setUsername("test");
        userInfoDto.setPassword("123");

        userServiceDetails.signupUser(userInfoDto);
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("test");
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(categoryRequestDto);
        ProductRequestDto productRequestDto = new ProductRequestDto("test","test product",BigDecimal.ONE,categoryResponseDto.getId());
        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto,categoryResponseDto.getId(),null);
        AddCartItemDto addCartItemDto = new AddCartItemDto(productResponseDto.getId(), 12);
        CartResponseDto cartResponseDto = cartService.addCartItem("test",addCartItemDto);
        CartItem cartItem = cartItemRepository.findAll().get(0);

        Assertions.assertThrows(CartItemNotFoundException.class,()->{
        cartService.deleteCartItem(100);
        });

    }
    @Test
    void deleteCartTest_valid() throws FileUploadException {
        UserInfoDto userInfoDto = new UserInfoDto("test",9885298032L,"test@yopmail.com");
        userInfoDto.setUsername("test");
        userInfoDto.setPassword("123");

        userServiceDetails.signupUser(userInfoDto);
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("test");
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(categoryRequestDto);
        ProductRequestDto productRequestDto = new ProductRequestDto("test","test product",BigDecimal.ONE,categoryResponseDto.getId());
        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto,categoryResponseDto.getId(),null);
        AddCartItemDto addCartItemDto = new AddCartItemDto(productResponseDto.getId(), 12);
        CartResponseDto cartResponseDto = cartService.addCartItem("test",addCartItemDto);
        Cart cart = cartRepository.findAll().get(0);
        boolean isDeleted = cartService.deleteCart(cart.getId());
        Assertions.assertTrue(isDeleted);
    }

    @Test
    void deleteCartTest_Invalid() throws FileUploadException {
        UserInfoDto userInfoDto = new UserInfoDto("test",9885298032L,"test@yopmail.com");
        userInfoDto.setUsername("test");
        userInfoDto.setPassword("123");

        userServiceDetails.signupUser(userInfoDto);
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("test");
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(categoryRequestDto);
        ProductRequestDto productRequestDto = new ProductRequestDto("test","test product",BigDecimal.ONE,categoryResponseDto.getId());
        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto,categoryResponseDto.getId(),null);
        AddCartItemDto addCartItemDto = new AddCartItemDto(productResponseDto.getId(), 12);
        CartResponseDto cartResponseDto = cartService.addCartItem("test",addCartItemDto);
        Cart cart = cartRepository.findAll().get(0);

       Assertions.assertThrows(CartNotFoundException.class,()->{
         cartService.deleteCart(123);
       });
    }


}
