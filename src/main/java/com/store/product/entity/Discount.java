package com.store.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // e.g., "BUY_ONE_GET_HALF_OFF"
    private int quantityRequired; // e.g., 1
    private double discountPercentage; // e.g., 50%

    //    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;
//    @ManyToMany(mappedBy = "discounts")
//    private Set<Product> products;
}
