package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for OrderItem entity
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

