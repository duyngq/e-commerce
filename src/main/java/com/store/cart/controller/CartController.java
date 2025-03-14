package com.store.cart.controller;

import com.store.cart.model.Receipt;
import com.store.cart.model.request.CartItemRequest;
import com.store.cart.model.response.CartResponse;
import com.store.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    @Autowired
    private CartService cartService;

    @DeleteMapping("/remove")
    public ResponseEntity<CartResponse> removeFromCart(@RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.removeFromCart(request));
    }

    // 🔹 View cart details
    @GetMapping
    public ResponseEntity<CartResponse> viewCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    // 🔹 Checkout: Calculate total price with discounts
    @PostMapping("/checkout")
    public ResponseEntity<CartResponse> checkout() {
        return ResponseEntity.ok(cartService.checkout());
    }
}
