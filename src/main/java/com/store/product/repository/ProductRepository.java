package com.store.product.repository;

import com.store.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT COUNT(p) FROM Product p WHERE p.id IN :ids")
    long countByIdIn(@Param("ids") Set<Long> ids);

    List<Product> findAllByIdIn(Set<Long> productIds);
}
