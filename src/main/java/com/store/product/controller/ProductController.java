package com.store.product.controller;

import com.store.product.model.request.ProductDiscountRequest;
import com.store.product.model.request.ProductRequest;
import com.store.product.model.response.ProductResponse;
import com.store.product.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/products")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeProducts(@RequestBody Set<Long> productIds) {
        productService.removeProduct(productIds);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<List<ProductDiscountRequest>> updateDiscount(@RequestBody List<ProductDiscountRequest> productDiscountRequest) {
        return ResponseEntity.ok(productService.updateDiscount(productDiscountRequest));
    }
}
