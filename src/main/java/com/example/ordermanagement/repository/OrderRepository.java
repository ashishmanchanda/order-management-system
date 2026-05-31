package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Order entity
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find order by order number
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * Find all orders by status
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    /**
     * Find all orders by customer email
     */
    Page<Order> findByCustomerEmail(String customerEmail, Pageable pageable);

    /**
     * Find all pending orders
     */
    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING'")
    List<Order> findPendingOrders();

    /**
     * Find all orders, paginated
     */
    Page<Order> findAll(Pageable pageable);

    /**
     * Count orders by status
     */
    long countByStatus(OrderStatus status);
}

