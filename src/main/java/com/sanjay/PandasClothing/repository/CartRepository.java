package com.sanjay.PandasClothing.repository;

import com.sanjay.PandasClothing.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // For order placing
    List<Cart> findByUserEmail(String userEmail);
    void deleteByUserEmail(String userEmail);

    // For cart add / wishlist move
    Optional<Cart> findByUserEmailAndProductIdAndSize(
            String userEmail,
            Long productId,
            String size
    );

//    List<Cart> findByUserEmail(String userEmail);
}