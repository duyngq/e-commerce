package com.store.product.discount.rule.impl;

import com.store.cart.entity.CartItem;
import com.store.product.discount.rule.DiscountRule;
import com.store.product.entity.Discount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BuyNGetMFreeRule implements DiscountRule {

    @Override
    public boolean supports(String dealType) {
        return "BUY_N_GET_M_FREE".equalsIgnoreCase(dealType);
    }

    @Override
    public BigDecimal applyDiscount(CartItem cartItem, Discount discount, BigDecimal originalTotal) {
        // e.g., if quantity >= discount.quantityRequired => M free items
        // Example: "Buy 3 get 1 free"
        if (cartItem.getQuantity() >= discount.getQuantityRequired()) {
            int free = discount.getFreeQuantity(); // e.g., 1
            int actualPaidQty = cartItem.getQuantity() - free;
            if (actualPaidQty < 0) {
                actualPaidQty = 0;
            }
            BigDecimal priceEach = cartItem.getProduct().getPrice();
            return priceEach.multiply(BigDecimal.valueOf(actualPaidQty));
        }
        return originalTotal;
    }
}