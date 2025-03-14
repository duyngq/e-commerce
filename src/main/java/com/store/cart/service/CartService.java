package com.store.cart.service;

import com.store.cart.model.Receipt;

public interface CartService {
    void addToCart(Long userId, Long productId, int quantity);

    Receipt calculateReceipt(Long userId);
}
