package com.store.cart.service.impl;

import com.store.auth.entity.User;
import com.store.auth.repository.UserRepository;
import com.store.auth.service.AuthService;
import com.store.cart.entity.Cart;
import com.store.cart.entity.CartItem;
import com.store.cart.model.mapper.CartMapper;
import com.store.cart.model.request.CartItemRequest;
import com.store.cart.model.response.CartResponse;
import com.store.cart.repository.CartRepository;
import com.store.cart.service.CartService;
import com.store.product.discount.DiscountRuleEngine;
import com.store.product.entity.Discount;
import com.store.product.entity.Product;
import com.store.product.repository.DiscountRepository;
import com.store.product.repository.ProductRepository;
import com.store.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CartMapper cartMapper; // Use MapStruct to map DTOs

    private final AuthService authService;

    private final DiscountRuleEngine discountRuleEngine;

    public CartServiceImpl(CartRepository cartRepository, ProductService productService,
                           CartMapper cartMapper, AuthService authService,
                           DiscountRuleEngine discountRuleEngine) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.cartMapper = cartMapper;
        this.authService = authService;
        this.discountRuleEngine = discountRuleEngine;
    }

    @Override
    public CartResponse getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse checkout(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();

            BigDecimal itemBasePrice = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            // Let the rule engine apply the correct discount
            BigDecimal discountedTotal = discountRuleEngine.applyDiscounts(item, itemBasePrice);

            total = total.add(discountedTotal);
        }

        cart.setTotalPrice(total);
        cartRepository.save(cart);

        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse addProductsToCart(List<CartItemRequest> items) {
        Cart cart =Cart.builder().user(getCurrentUser()).totalPrice(BigDecimal.ZERO).build();

        for (CartItemRequest itemRequest : items) {
            Product product = productService.getProductEntity(itemRequest.getProductId());

            List<CartItem> cartItemList = cart.getItems();
            if (cartItemList == null) {
                cartItemList = new ArrayList<>();
                cart.setItems(cartItemList);
            }
            // Find existing item or create a new one
            CartItem cartItem = cartItemList.stream()
                    .filter(ci -> ci.getProduct() != null && ci.getProduct().equals(product))
                    .findFirst()
                    .orElse(CartItem.builder()
                            .product(product)
                            .quantity(0)
                            .cart(cart)
                            .build());

            // Update quantity
            cartItem.setQuantity(cartItem.getQuantity() + itemRequest.getQuantity());

            // Ensure item is in cart’s item list
            if (!cartItemList.contains(cartItem)) {
                cartItemList.add(cartItem);
            }
        }

        cartRepository.save(cart);

        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse updateProductsFromCart(Long cartId, List<CartItemRequest> items) {
        // 1) Fetch user's cart or throw an exception if not found
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // 2) For each item in the list, reduce quantity or remove completely
        for (CartItemRequest itemReq : items) {
            Product product = productService.getProductEntity(itemReq.getProductId());

            // Find the cart item that matches the product
            CartItem existingItem = cart.getItems().stream()
                    .filter(ci -> ci.getProduct().equals(product))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                int newQuantity = itemReq.getQuantity();

                if (newQuantity <= 0) {
                    // If new quantity is zero or negative, remove the item entirely
                    cart.getItems().remove(existingItem);
                } else {
                    // Otherwise, update the quantity
                    existingItem.setQuantity(newQuantity);
                }
            } else {
                CartItem newCartItem = CartItem.builder()
                        .product(product)
                        .quantity(itemReq.getQuantity())
                        .cart(cart)
                        .build();

                // Ensure item is in cart’s item list
                if (!cart.getItems().contains(newCartItem)) {
                    cart.getItems().add(newCartItem);
                }
            }
        }
        cart.setTotalPrice(BigDecimal.ZERO);
        // 3) Save the updated cart
        cartRepository.save(cart);

        // 4) Return the updated cart as a response
        return cartMapper.toResponse(cart);
    }

    private BigDecimal applyDiscount(BigDecimal price, Discount discount) {
        if (discount.getPercentage() > 0) {
            return price.multiply(BigDecimal.valueOf((100 - discount.getPercentage()) / 100.0));
        }
        return price;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return authService.findByUsername(username);
    }
}
