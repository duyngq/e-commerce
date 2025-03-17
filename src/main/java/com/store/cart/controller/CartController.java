package com.store.cart.controller;

import com.store.cart.model.request.CartItemRequest;
import com.store.cart.model.response.CartResponse;
import com.store.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addProductsToCart(@RequestBody List<CartItemRequest> items) {
        CartResponse addedCart = cartService.addProductsToCart(items);
        return ResponseEntity.ok(addedCart);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartResponse> removeProductsFromCart(@PathVariable Long id, @RequestBody List<CartItemRequest> items) {
        CartResponse updatedCart = cartService.removeProductsFromCart(id, items);
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping
    public ResponseEntity<CartResponse> viewCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @GetMapping("/checkout/{id}")
    public ResponseEntity<CartResponse> checkout(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.checkout(id));
    }
}
