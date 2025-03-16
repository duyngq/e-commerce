package com.store.cart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.product.entity.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "FK_CART_ITEM_PRODUCT"))
    private Product product;
    private int quantity;

    @ToString.Exclude
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", foreignKey = @ForeignKey(name = "FK_CART_ITEM_CART"))
    private Cart cart;

    @Transient
    public BigDecimal getSubTotal() {
        BigDecimal unitPrice = product.getPrice();
        BigDecimal quantity = BigDecimal.valueOf(getQuantity());
        return unitPrice.multiply(quantity);
    }
}
