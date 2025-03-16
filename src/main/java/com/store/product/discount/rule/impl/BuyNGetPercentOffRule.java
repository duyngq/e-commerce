package com.store.product.discount.rule.impl;

import com.store.cart.entity.CartItem;
import com.store.product.discount.rule.DiscountRule;
import com.store.product.entity.Discount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BuyNGetPercentOffRule implements DiscountRule {

    @Override
    public boolean supports(String dealType) {
        return "BUY_N_GET_PERCENT_OFF".equalsIgnoreCase(dealType);
    }

    @Override
    public BigDecimal applyDiscount(CartItem cartItem, Discount discount, BigDecimal originalTotal) {
        // If quantity >= 2 => discount half of 1 item
        if (cartItem.getQuantity() >= discount.getQuantityRequired()) {
            BigDecimal nGetPercentOffItem = BigDecimal.valueOf(cartItem.getQuantity()-discount.getQuantityRequired()).multiply(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(discount.getPercentage()/100)));
            return originalTotal.subtract(nGetPercentOffItem);
        }
        return originalTotal;
    }
}
