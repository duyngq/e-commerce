package com.store.cart.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemRequest {
    private Long productId;
    private int quantity;
}
