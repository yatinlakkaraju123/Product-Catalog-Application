package com.yatindevhub.ecommerce.dto.Cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCartItemDto {
    private long productId;
    @Min(value = 0, message = "quantity should be at least 0")
    @Max(value = 100, message = "quantity should not exceed 100")
    private int quantity;

}
