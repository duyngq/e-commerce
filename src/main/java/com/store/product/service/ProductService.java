package com.store.product.service;

import com.store.product.model.request.ProductDiscountRequest;
import com.store.product.model.request.ProductRequest;
import com.store.product.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ProductService {

    Page<ProductResponse> getAllProducts(Pageable pageable);

    List<ProductDiscountRequest> updateDiscount(List<ProductDiscountRequest> productDiscountRequest);

    void removeProduct(Set<Long> productIds);

    ProductResponse addProduct(ProductRequest productRequest);

}
