package com.store.cart.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CartResponse {
    private List<CartItemResponse> items;
    private BigDecimal totalPrice;
}
