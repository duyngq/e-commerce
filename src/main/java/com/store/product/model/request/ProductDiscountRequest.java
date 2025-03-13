package com.store.product.model.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductDiscountRequest {
    private Long productId;
    private Long discountId;
}
