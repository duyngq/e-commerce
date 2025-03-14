package com.store.product.controller;

import com.store.product.model.request.DiscountRequest;
import com.store.product.model.response.DiscountResponse;
import com.store.product.service.impl.DiscountServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/discounts")
@PreAuthorize("hasAuthority('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class DiscountController {
    private DiscountServiceImpl discountService;

    public DiscountController(DiscountServiceImpl discountService) {
        this.discountService = discountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountResponse> getDiscount(@PathVariable Long id) {
        return ResponseEntity.ok(discountService.getDiscount(id));
    }

    @PostMapping
    public ResponseEntity<DiscountResponse> createDiscount(@RequestBody DiscountRequest discount) {
        return ResponseEntity.ok(discountService.createDiscount(discount));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeProducts(@RequestBody Set<Long> discountIds) {
        discountService.remove(discountIds);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountResponse> updateDiscount(@PathVariable Long id, @RequestBody DiscountRequest discountRequest) {
        return ResponseEntity.ok(discountService.updateDiscount(id, discountRequest));
    }
}
