package com.store.product.repository;

import com.store.product.entity.Product;
import com.store.product.entity.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
}
