package com.store.product.model.request;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Getter
public class ProductRequest {
    private String name;
    private BigDecimal price;
//    private Set<Long> discountIds; // Instead of actual Discount objects
}
