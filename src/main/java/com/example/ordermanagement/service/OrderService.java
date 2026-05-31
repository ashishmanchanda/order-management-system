package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.CreateOrderDTO;
import com.example.ordermanagement.dto.OrderDTO;
import com.example.ordermanagement.dto.PagedResponse;
import com.example.ordermanagement.entity.OrderStatus;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Order operations
 */
public interface OrderService {

    /**
     * Create a new order
     */
    OrderDTO createOrder(CreateOrderDTO createOrderDTO);

    /**
     * Get order by ID
     */
    OrderDTO getOrderById(Long id);

    /**
     * Get order by order number
     */
    OrderDTO getOrderByOrderNumber(String orderNumber);

    /**
     * Get all orders with pagination
     */
    PagedResponse<OrderDTO> getAllOrders(Pageable pageable);

    /**
     * Get orders by status with pagination
     */
    PagedResponse<OrderDTO> getOrdersByStatus(OrderStatus status, Pageable pageable);

    /**
     * Get orders by customer email with pagination
     */
    PagedResponse<OrderDTO> getOrdersByCustomerEmail(String customerEmail, Pageable pageable);

    /**
     * Update order status
     */
    OrderDTO updateOrderStatus(Long id, OrderStatus status);

    /**
     * Cancel an order (only if it's in PENDING status)
     */
    OrderDTO cancelOrder(Long id);

    /**
     * Get count of orders by status
     */
    long getOrderCountByStatus(OrderStatus status);
}

