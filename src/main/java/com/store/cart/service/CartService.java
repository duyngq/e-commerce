package com.store.cart.service;

import com.store.cart.model.Receipt;
import com.store.cart.model.request.CartItemRequest;
import com.store.cart.model.response.CartResponse;

public interface CartService {
//    void addToCart(Long userId, Long productId, int quantity);
//    Receipt calculateReceipt(Long userId);

    CartResponse addToCart(CartItemRequest request);
    CartResponse removeFromCart(CartItemRequest request);
    CartResponse getCart();
    CartResponse checkout();
}
