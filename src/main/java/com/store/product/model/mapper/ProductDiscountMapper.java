package com.store.product.model.mapper;

import com.store.product.entity.Discount;
import com.store.product.entity.Product;
import com.store.product.entity.ProductDiscount;
import com.store.product.model.request.ProductDiscountRequest;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface ProductDiscountMapper {

    ProductDiscountMapper INSTANCE = Mappers.getMapper(ProductDiscountMapper.class);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProductIdToProduct")
//    @Mapping(target = "discount", source = "discountId", qualifiedByName = "mapDiscountIdToDiscount")
//    ProductDiscount toEntity(ProductDiscountRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProductIdMapToProduct")
    @Mapping(target = "discount", source = "discountId", qualifiedByName = "mapDiscountIdMapToDiscount")
    ProductDiscount toEntity(ProductDiscountRequest request, @Context Map<Long, Product> productsByIds, @Context Map<Long, Discount> discountsByIds);


    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "discountId", source = "discount.id")
    ProductDiscountRequest toResponse(ProductDiscount productDiscount);

    List<ProductDiscount> toEntityList(List<ProductDiscountRequest> productDiscountRequests, @Context Map<Long, Product> productsByIds, @Context Map<Long, Discount> discountsByIds);

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

    @Named("mapProductIdMapToProduct")
    default Product mapProductIdMapToProduct(Long productId, @Context Map<Long, Product> productsByIds) {
        if (productId == null) {
            return null;
        }
        return productsByIds.get(productId);
//        return Product.builder().id(productId).build(); // Create a reference object
    }

    @Named("mapDiscountIdMapToDiscount")
    default Discount mapDiscountIdMapToDiscount(Long discountId, @Context Map<Long, Discount> discountsByIds) {
        if (discountId == null) {
            return null;
        }

        return discountsByIds.get(discountId);
//        return Discount.builder().id(discountId).build(); // Create a reference object
    }
}
