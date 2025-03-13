package com.store.product.model.request;

import com.store.product.entity.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Getter
public class DiscountRequest {
    private String type;
    private int quantityRequired;
    private double discountPercentage;
    private Set<Long> productId;
}
