package com.store.product.model.mapper;

import com.store.product.entity.Discount;
import com.store.product.entity.Product;
import com.store.product.entity.ProductDiscount;
import com.store.product.model.request.ProductDiscountRequest;
import com.store.product.model.request.ProductRequest;
import com.store.product.model.response.DiscountResponse;
import com.store.product.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductDiscountMapper {

    ProductDiscountMapper INSTANCE = Mappers.getMapper(ProductDiscountMapper.class);

    @Mapping(target = "id", ignore = true) // Ignore ID as it is generated
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProductIdToProduct")
    @Mapping(target = "discount", source = "discountId", qualifiedByName = "mapDiscountIdToDiscount")
    ProductDiscount toEntity(ProductDiscountRequest request);


    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "discountId", source = "discount.id")
    ProductDiscountRequest toResponse(ProductDiscount productDiscount);

    List<ProductDiscount> toEntityList(List<ProductDiscountRequest> productDiscountRequests);

    @Named("mapProductIdToProduct")
    default Product mapProductIdToProduct(Long productId) {
        if (productId == null) {
            return null;
        }
        return Product.builder().id(productId).build(); // Create a reference object
    }

    @Named("mapDiscountIdToDiscount")
    default Discount mapDiscountIdToDiscount(Long discountId) {
        if (discountId == null) {
            return null;
        }
        return Discount.builder().id(discountId).build(); // Create a reference object
    }
}
