package com.store.product.controller;

import com.store.product.model.request.DiscountRequest;
import com.store.product.model.request.ProductRequest;
import com.store.product.model.response.DiscountResponse;
import com.store.product.model.response.ProductResponse;
import com.store.product.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<Void> removeProducts(@RequestBody List<Long> productIds) {
        productService.removeProduct(productIds);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/discount")
    public ResponseEntity<DiscountResponse> addDiscount(@PathVariable Long id, @RequestBody DiscountRequest discount) {
        return ResponseEntity.ok(productService.addDiscount(id, discount));
    }
}
