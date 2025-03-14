package com.store.product.service.impl;

import com.store.product.entity.Discount;
import com.store.product.model.mapper.DiscountMapper;
import com.store.product.model.request.DiscountRequest;
import com.store.product.model.response.DiscountResponse;
import com.store.product.repository.DiscountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DiscountService implements com.store.product.service.DiscountService {

    private DiscountRepository discountRepository;

    private final DiscountMapper discountMapper;

    public DiscountService(DiscountRepository discountRepository, DiscountMapper discountMapper) {
        this.discountRepository = discountRepository;
        this.discountMapper = discountMapper;
    }

    public DiscountResponse createDiscount(DiscountRequest request) {
        Discount discount = discountMapper.toEntity(request);
        Discount savedDiscount = discountRepository.save(discount);
        return discountMapper.toResponse(savedDiscount);
    }

    public void remove(Set<Long> ids) {
        long count = discountRepository.countByIdIn(ids);
        if (count != ids.size()) {
            throw new EntityNotFoundException("No all products found for the given IDs: " + ids);
        }
        discountRepository.deleteAllByIdInBatch(ids);
    }
}
