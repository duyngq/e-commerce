package com.store.cart.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private String productName;
    private int quantity;
//    private BigDecimal totalPrice;
}
