package com.yatindevhub.ecommerce.dto.Cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeQuantityDto {

    private long id;
    @Min(value = 0, message = "quantity must be at least 0")
    @Max(value = 100, message = "quantity must not exceed 100")
    private Integer quantity;
}
