package com.store.cart.model;

import com.store.cart.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class Receipt {
    List<CartItem> cartItems;
    BigDecimal total;
}
