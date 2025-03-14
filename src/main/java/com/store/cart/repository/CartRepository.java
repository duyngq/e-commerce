package com.store.cart.repository;

import com.store.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
//    @Query("SELECT COUNT(d) FROM Discount d WHERE d.id IN :ids")
//    long countByIdIn(@Param("ids") Set<Long> ids);
}
