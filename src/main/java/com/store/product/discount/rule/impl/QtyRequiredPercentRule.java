package com.store.product.discount.rule.impl;

import com.store.cart.entity.CartItem;
import com.store.product.discount.rule.DiscountRule;
import com.store.product.entity.Discount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class QtyRequiredPercentRule implements DiscountRule {

    @Override
    public boolean supports(String dealType) {
        return "QTY_REQUIRED_PERCENT".equalsIgnoreCase(dealType);
    }

    @Override
    public BigDecimal applyDiscount(CartItem cartItem, Discount discount, BigDecimal originalTotal) {
        // TODO: need to revise this rule
        return originalTotal;
    }
}
