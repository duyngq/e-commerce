package com.store.cart.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CartItemResponse {
    private String productName;
    private int quantity;
    private BigDecimal totalPrice;
}
