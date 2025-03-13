package com.store.product.model.mapper;

import com.store.product.entity.Discount;
import com.store.product.entity.Product;
import com.store.product.entity.ProductDiscount;
import com.store.product.model.request.ProductRequest;
import com.store.product.model.response.DiscountResponse;
import com.store.product.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id", ignore = true) // ID is generated, so ignore it in requests
//    @Mapping(target = "discounts", ignore = true) // Discounts are mapped separately
    Product toEntity(ProductRequest request);

    @Mapping(target = "discounts", source = "productDiscounts", qualifiedByName = "mapDiscounts")
    ProductResponse toResponse(Product product);

    @Named("mapDiscounts")
    default Set<DiscountResponse> mapDiscounts(Set<ProductDiscount> productDiscounts) {
        return productDiscounts.stream()
                .map(pd -> new DiscountResponse(pd.getDiscount().getId(), pd.getDiscount().getType(), pd.getDiscount().getQuantityRequired(), pd.getDiscount().getDiscountPercentage()))
                .collect(Collectors.toSet());
    }

}
