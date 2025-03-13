package com.store.product.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class ProductDiscountRequest {
    private Long productId;
    private Long discountId;
}
