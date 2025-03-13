package com.store.product.service;

import com.store.product.entity.Discount;
import com.store.product.entity.Product;
import com.store.product.entity.ProductDiscount;
import com.store.product.model.mapper.DiscountMapper;
import com.store.product.model.mapper.ProductDiscountMapper;
import com.store.product.model.mapper.ProductMapper;
import com.store.product.model.request.DiscountRequest;
import com.store.product.model.request.ProductDiscountRequest;
import com.store.product.model.request.ProductRequest;
import com.store.product.model.response.DiscountResponse;
import com.store.product.model.response.ProductResponse;
import com.store.product.repository.DiscountRepository;
import com.store.product.repository.ProductDiscountRepository;
import com.store.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private ProductRepository productRepository;

    private DiscountRepository discountRepository;

    private ProductDiscountRepository productDiscountRepository;
    private ProductDiscountMapper productDiscountMapper;

    private ProductMapper productMapper;

    private DiscountMapper discountMapper;

    public ProductService(ProductRepository productRepository, DiscountRepository discountRepository, ProductMapper productMapper, DiscountMapper discountMapper,
                           ProductDiscountRepository productDiscountRepository, ProductDiscountMapper productDiscountMapper) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.productMapper = productMapper;
        this.discountMapper = discountMapper;
        this.productDiscountRepository = productDiscountRepository;
        this.productDiscountMapper = productDiscountMapper;
    }

    @Transactional
    public ProductResponse addProduct(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    public void removeProduct(Set<Long> productIds) {
        long count = productRepository.countByIdIn(productIds);
        if (count != productIds.size()) {
            throw new EntityNotFoundException("No all products found for the given IDs: " + productIds);
        }
        productRepository.deleteAllByIdInBatch(productIds);
    }

    public List<ProductDiscountRequest> addDiscount(List<ProductDiscountRequest> productDiscountRequest) {
        // filter to a list of product id and list of discount request
        Set<Long> productIds = productDiscountRequest.stream()
                .map(ProductDiscountRequest::getProductId)
                .collect(Collectors.toSet());
        long productCount = productRepository.countByIdIn(productIds);
        if (productCount != productIds.size()) {
            throw new EntityNotFoundException("No all products found for the given IDs: " + productIds);
        }

        Set<Long> discountIds = productDiscountRequest.stream()
                .map(ProductDiscountRequest::getDiscountId)
                .collect(Collectors.toSet());
        long discountCount = discountRepository.countByIdIn(discountIds);
        if (discountCount != productIds.size()) {
            throw new EntityNotFoundException("No all discounts found for the given IDs: " + discountIds);
        }
        List<ProductDiscount> productDiscounts = productDiscountRepository.saveAll(productDiscountMapper.toEntityList(productDiscountRequest));
        return productDiscounts.stream()
                .map(productDiscountMapper::toResponse)
                .collect(Collectors.toList());
    }
}
