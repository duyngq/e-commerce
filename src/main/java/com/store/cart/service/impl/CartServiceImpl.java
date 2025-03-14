package com.store.cart.service.impl;

import com.store.auth.entity.User;
import com.store.cart.entity.Cart;
import com.store.cart.entity.CartItem;
import com.store.cart.model.Receipt;
import com.store.cart.repository.CartRepository;
import com.store.cart.service.CartService;
import com.store.product.entity.Product;
import com.store.product.entity.ProductDiscount;
import com.store.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(null, new ArrayList<>(), new User()));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        cart.getCartItems().add(item);

        cartRepository.save(cart);
    }

    @Override
    public Receipt calculateReceipt(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem item : cart.getCartItems()) {
            BigDecimal itemPrice = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            // Apply discounts if available
            for (ProductDiscount discount : item.getProduct().getDiscounts()) {
                if (item.getQuantity() >= discount.getDiscount().getQuantityRequired()) {
                    BigDecimal discountAmount = item.getProduct().getPrice()
                            .multiply(discount.getDiscount().getDiscountPercentage());
                    itemPrice = itemPrice.subtract(discountAmount);
                }
            }

            totalPrice = totalPrice.add(itemPrice);
        }

        return new Receipt(cart.getCartItems(), totalPrice);
    }
}
