package com.store.product.discount;

import com.store.cart.entity.CartItem;
import com.store.product.discount.rule.DiscountRule;
import com.store.product.entity.Discount;
import com.store.product.entity.ProductDiscount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DiscountRuleEngine {

    private final List<DiscountRule> rules;

    public DiscountRuleEngine(List<DiscountRule> rules) {
        this.rules = rules;
    }

    public BigDecimal applyDiscounts(CartItem item, BigDecimal itemTotal) {
        // For each discount on the product, find the matching rule and apply it
        for (ProductDiscount pd : item.getProduct().getDiscounts()) {
            // find the rule
            for (DiscountRule rule : rules) {
                if (rule.supports(pd.getDiscount().getType())) {
                    itemTotal = rule.applyDiscount(item, pd.getDiscount(), itemTotal);
                     break; // TODO: Currently, only one discount can apply
                }
            }
        }
        return itemTotal;
    }
}
