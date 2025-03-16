package com.store.cart.service;

import com.store.cart.model.request.CartItemRequest;
import com.store.cart.model.response.CartResponse;

import java.util.List;

public interface CartService {
//    void addToCart(Long userId, Long productId, int quantity);
//    Receipt calculateReceipt(Long userId);

    CartResponse addToCart(CartItemRequest request);
    CartResponse removeFromCart(CartItemRequest request);
    CartResponse getCart();
    CartResponse checkout(Long cartId);

    CartResponse addProductsToCart(List<CartItemRequest> items);

    CartResponse removeProductsFromCart(Long cartId, List<CartItemRequest> items);
}
