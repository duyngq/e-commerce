package com.store.product.discount.rule;

import com.store.cart.entity.CartItem;
import com.store.product.entity.Discount;

import java.math.BigDecimal;

public interface DiscountRule {
    /**
     * Applies discount logic to the given itemTotal
     * based on cartItem quantity, product price, etc.
     *
     * @param cartItem       the cart item
     * @param discount       the discount entity (includes dealType, quantityRequired, discountPercentage, etc.)
     * @param originalTotal  the itemTotal before discount
     * @return new discounted total
     */
    BigDecimal applyDiscount(CartItem cartItem, Discount discount, BigDecimal originalTotal);

    /**
     * Indicates if this rule can handle the given discount.dealType
     */
    boolean supports(String dealType);
}
