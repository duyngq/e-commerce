package com.store.product.model.response;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Getter
public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Set<DiscountResponse> discounts;
}
