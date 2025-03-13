package com.store.product.service;

import com.store.product.entity.Product;
import com.store.product.entity.ProductDiscount;
import com.store.product.model.mapper.DiscountMapper;
import com.store.product.model.mapper.ProductDiscountMapper;
import com.store.product.model.mapper.ProductMapper;
import com.store.product.model.request.ProductDiscountRequest;
import com.store.product.model.request.ProductRequest;
import com.store.product.model.response.ProductResponse;
import com.store.product.repository.DiscountRepository;
import com.store.product.repository.ProductDiscountRepository;
import com.store.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    public ProductService(ProductRepository productRepository, DiscountRepository discountRepository, ProductMapper productMapper, DiscountMapper discountMapper, ProductDiscountRepository productDiscountRepository, ProductDiscountMapper productDiscountMapper) {
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

    public List<ProductDiscountRequest> updateDiscount(List<ProductDiscountRequest> productDiscountRequest) {
        // filter to a list of product id and list of discount request
        Set<Long> productIds = productDiscountRequest.stream().map(ProductDiscountRequest::getProductId).collect(Collectors.toSet());
        long productCount = productRepository.countByIdIn(productIds);
        if (productCount != productIds.size()) {
            throw new EntityNotFoundException("No all products found for the given IDs: " + productIds);
        }

        Set<Long> discountIds = productDiscountRequest.stream().map(ProductDiscountRequest::getDiscountId).collect(Collectors.toSet());
        long discountCount = discountRepository.countByIdIn(discountIds);
        if (discountCount != productIds.size()) {
            throw new EntityNotFoundException("No all discounts found for the given IDs: " + discountIds);
        }
        // Get all discounts for the products from the database
        List<ProductDiscount> productDiscountsInDB = productDiscountRepository.findByProductIdIn(productIds);


        List<ProductDiscountRequest> updateProductDiscountList = new ArrayList<>();
        List<ProductDiscountRequest> deleteProductDiscountList = new ArrayList<>();
        separateToUpdateAndDeleteProductDiscounts(productDiscountRequest, productDiscountsInDB, updateProductDiscountList, deleteProductDiscountList);
        List<ProductDiscount> productDiscounts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(updateProductDiscountList)) {
            productDiscounts = productDiscountRepository.saveAll(productDiscountMapper.toEntityList(updateProductDiscountList));
        }
        if (!CollectionUtils.isEmpty(deleteProductDiscountList)) {
            productDiscountRepository.deleteInBatch(productDiscountMapper.toEntityList(deleteProductDiscountList));
        }
        return productDiscounts.stream().map(productDiscountMapper::toResponse).collect(Collectors.toList());
    }

    private void separateToUpdateAndDeleteProductDiscounts(List<ProductDiscountRequest> productDiscountRequest, List<ProductDiscount> productDiscountsInDB, List<ProductDiscountRequest> updateProductDiscountList, List<ProductDiscountRequest> deleteProductDiscountList) {
        // Convert existing discounts to a map of productId to set of discountIds
        Map<Long, Set<Long>> discountsInDBMap = productDiscountsInDB.stream().collect(Collectors.groupingBy(pd -> pd.getProduct().getId(), Collectors.mapping(pd -> pd.getDiscount().getId(), Collectors.toSet())));

        // Convert request discounts to a map of productId to set of discountIds
        Map<Long, Set<Long>> requestDiscountsMap = productDiscountRequest.stream().collect(Collectors.groupingBy(ProductDiscountRequest::getProductId, Collectors.mapping(ProductDiscountRequest::getDiscountId, Collectors.toSet())));

        // Compare request discounts with existing discounts
        for (Map.Entry<Long, Set<Long>> entry : requestDiscountsMap.entrySet()) {
            Long productId = entry.getKey();
            Set<Long> requestDiscountIds = entry.getValue();
            Set<Long> existingDiscountIds = discountsInDBMap.getOrDefault(productId, Collections.emptySet());

            // Add to update list if in request but not in existing
            for (Long discountId : requestDiscountIds) {
                if (!existingDiscountIds.contains(discountId)) {
                    updateProductDiscountList.add(new ProductDiscountRequest(productId, discountId));
                }
            }

            // Add to delete list if in existing but not in request
            for (Long discountId : existingDiscountIds) {
                if (!requestDiscountIds.contains(discountId)) {
                    deleteProductDiscountList.add(new ProductDiscountRequest(productId, discountId));
                }
            }
        }
    }
}
