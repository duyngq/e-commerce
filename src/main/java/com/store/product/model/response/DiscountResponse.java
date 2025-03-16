package com.store.product.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@Getter
@AllArgsConstructor
public class DiscountResponse {
    private Long id;
    private String type;
    private int quantityRequired;
    private double percentage;
}
