package com.store.cart.model.mapper;

import com.store.cart.entity.Cart;
import com.store.cart.entity.CartItem;
import com.store.cart.model.response.CartItemResponse;
import com.store.cart.model.response.CartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(target = "totalPrice", source = "totalPrice", qualifiedByName = "formatPrice")
    CartResponse toResponse(Cart cart);

    @Mapping(target = "productName", source = "product.name")
//    @Mapping(target = "totalPrice", source = "totalPrice", qualifiedByName = "formatPrice")
    CartItemResponse toCartItemResponse(CartItem cartItem);

    default List<CartItemResponse> mapCartItems(List<CartItem> items) {
        return items.stream()
                .map(this::toCartItemResponse)
                .collect(Collectors.toList());
    }

    @Named("formatPrice")
    default BigDecimal formatPrice(BigDecimal price) {
        return price.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
