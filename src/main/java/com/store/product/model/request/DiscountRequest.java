package com.store.product.model.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class DiscountRequest {
    private String type;
    private int quantityRequired;
    private double percentage;
}
