package com.store.product.model.request;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Getter
public class ProductRequest {
    private String name;
    private BigDecimal price;
    private List<ProductDiscountRequest> discounts; // Instead of actual Discount objects
}
