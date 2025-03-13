package com.store.product.model.response;

import com.store.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class DiscountResponse {
    private Long id;
    private String type;
    private int quantityRequired;
    private double discountPercentage;
}
