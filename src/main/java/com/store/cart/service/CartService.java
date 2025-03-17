package com.store.cart.service;

import com.store.cart.model.request.CartItemRequest;
import com.store.cart.model.response.CartResponse;

import java.util.List;

public interface CartService {
    CartResponse getCart();
    CartResponse checkout(Long cartId);

    CartResponse addProductsToCart(List<CartItemRequest> items);

    CartResponse removeProductsFromCart(Long cartId, List<CartItemRequest> items);
}
