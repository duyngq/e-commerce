package com.store.product.service;

import com.store.product.entity.Discount;
import com.store.product.entity.Product;
import com.store.product.model.mapper.DiscountMapper;
import com.store.product.model.mapper.ProductMapper;
import com.store.product.model.request.DiscountRequest;
import com.store.product.model.request.ProductRequest;
import com.store.product.model.response.DiscountResponse;
import com.store.product.model.response.ProductResponse;
import com.store.product.repository.DiscountRepository;
import com.store.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    private ProductRepository productRepository;

    private DiscountRepository discountRepository;

    private final ProductMapper productMapper;

    private DiscountMapper discountMapper;

    private ProductService(ProductRepository productRepository, DiscountRepository discountRepository, ProductMapper productMapper, DiscountMapper discountMapper) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.productMapper = productMapper;
        this.discountMapper = discountMapper;
    }

    public ProductResponse addProduct(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
//        return productRepository.save(product);
    }

    public void removeProduct(List<Long> productIds) {
        long count = productRepository.countByIdIn(productIds);
        if (count != productIds.size()) {
            throw new EntityNotFoundException("No all products found for the given IDs: " + productIds);
        }
        productRepository.deleteAllByIdInBatch(productIds);
    }

    public DiscountResponse addDiscount(Long productId, DiscountRequest discountRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Discount discount = discountMapper.toEntity(discountRequest);
        Set<Product> products = new HashSet<>();
        products.add(product);
        discountRepository.save(discount);
        return discountMapper.toResponse(discount);
    }
}
