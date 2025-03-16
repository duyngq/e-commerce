package com.store.product.model.request;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@Getter
public class DiscountRequest {
//    private String type;
//    private int quantityRequired;
//    private double percentage;
    private String type; // e.g., "BUY_ONE_GET_HALF_OFF"
    private int quantityRequired; // e.g., 1
    private int freeQuantity;          // For "Buy N get M free" deals
    private double percentage; // e.g., 50%
}
