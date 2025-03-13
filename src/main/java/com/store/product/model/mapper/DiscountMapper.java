package com.store.product.model.mapper;

import com.store.product.entity.Discount;
import com.store.product.model.request.DiscountRequest;
import com.store.product.model.response.DiscountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DiscountMapper {

    DiscountMapper INSTANCE = Mappers.getMapper(DiscountMapper.class);

    @Mapping(target = "id", ignore = true) // ID is generated, so ignore it in requests
    Discount toEntity(DiscountRequest request);

    DiscountResponse toResponse(Discount product);
}
