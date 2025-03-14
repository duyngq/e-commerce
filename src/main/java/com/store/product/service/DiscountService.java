package com.store.product.service;

import com.store.product.entity.Discount;
import com.store.product.model.request.DiscountRequest;
import com.store.product.model.response.DiscountResponse;

import java.util.List;
import java.util.Set;

public interface DiscountService {

    DiscountResponse createDiscount(DiscountRequest request);

    List<DiscountResponse> createDiscounts(List<DiscountRequest> requests);

    void remove(Set<Long> ids);

    DiscountResponse getDiscount(Long discountId);

    DiscountResponse updateDiscount(Long id, DiscountRequest discountRequest);
}
