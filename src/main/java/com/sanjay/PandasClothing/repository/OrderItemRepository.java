package com.sanjay.PandasClothing.repository;

import com.sanjay.PandasClothing.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}