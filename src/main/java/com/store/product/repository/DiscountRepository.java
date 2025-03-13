package com.store.product.repository;

import com.store.product.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    @Query("SELECT COUNT(d) FROM Discount d WHERE d.id IN :ids")
    long countByIdIn(@Param("ids") Set<Long> ids);
}
