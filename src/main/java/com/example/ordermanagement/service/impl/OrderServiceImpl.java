package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.dto.CreateOrderDTO;
import com.example.ordermanagement.dto.CreateOrderItemDTO;
import com.example.ordermanagement.dto.OrderDTO;
import com.example.ordermanagement.dto.PagedResponse;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.OrderItem;
import com.example.ordermanagement.entity.OrderStatus;
import com.example.ordermanagement.exception.OrderCancellationException;
import com.example.ordermanagement.exception.OrderNotFoundException;
import com.example.ordermanagement.mapper.OrderMapper;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of OrderService
 */
@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDTO createOrder(CreateOrderDTO createOrderDTO) {
        log.info("Creating new order for customer: {}", createOrderDTO.getCustomerName());

        // Create order entity
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setCustomerName(createOrderDTO.getCustomerName());
        order.setCustomerEmail(createOrderDTO.getCustomerEmail());
        order.setShippingAddress(createOrderDTO.getShippingAddress());
        order.setStatus(OrderStatus.PENDING);

        // Add order items
        for (CreateOrderItemDTO itemDTO : createOrderDTO.getOrderItems()) {
            OrderItem item = new OrderItem();
            item.setProductName(itemDTO.getProductName());
            item.setProductCode(itemDTO.getProductCode());
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setTotalPrice(itemDTO.getUnitPrice().multiply(new BigDecimal(itemDTO.getQuantity())));
            order.addOrderItem(item);
        }

        // Calculate total amount
        order.calculateTotalAmount();

        // Save order
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with ID: {} and order number: {}", savedOrder.getId(), savedOrder.getOrderNumber());

        return orderMapper.toOrderDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", id);
                    return new OrderNotFoundException("Order not found with ID: " + id);
                });
        return orderMapper.toOrderDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderByOrderNumber(String orderNumber) {
        log.info("Fetching order with order number: {}", orderNumber);
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> {
                    log.error("Order not found with order number: {}", orderNumber);
                    return new OrderNotFoundException("Order not found with order number: " + orderNumber);
                });
        return orderMapper.toOrderDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<OrderDTO> getAllOrders(Pageable pageable) {
        log.info("Fetching all orders - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Order> orders = orderRepository.findAll(pageable);
        return buildPagedResponse(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<OrderDTO> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        log.info("Fetching orders with status: {} - page: {}, size: {}", status, pageable.getPageNumber(), pageable.getPageSize());
        Page<Order> orders = orderRepository.findByStatus(status, pageable);
        return buildPagedResponse(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<OrderDTO> getOrdersByCustomerEmail(String customerEmail, Pageable pageable) {
        log.info("Fetching orders for customer email: {} - page: {}, size: {}", customerEmail, pageable.getPageNumber(), pageable.getPageSize());
        Page<Order> orders = orderRepository.findByCustomerEmail(customerEmail, pageable);
        return buildPagedResponse(orders);
    }

    @Override
    public OrderDTO updateOrderStatus(Long id, OrderStatus status) {
        log.info("Updating order status - ID: {}, new status: {}", id, status);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", id);
                    return new OrderNotFoundException("Order not found with ID: " + id);
                });
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        log.info("Order status updated successfully for ID: {}", id);
        return orderMapper.toOrderDTO(updatedOrder);
    }

    @Override
    public OrderDTO cancelOrder(Long id) {
        log.info("Attempting to cancel order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", id);
                    return new OrderNotFoundException("Order not found with ID: " + id);
                });

        // Check if order is in PENDING status
        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("Cannot cancel order ID: {} - Current status: {}", id, order.getStatus());
            throw new OrderCancellationException("Only PENDING orders can be cancelled. Current status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);
        log.info("Order cancelled successfully for ID: {}", id);
        return orderMapper.toOrderDTO(cancelledOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public long getOrderCountByStatus(OrderStatus status) {
        log.info("Getting count of orders with status: {}", status);
        return orderRepository.countByStatus(status);
    }

    /**
     * Generate unique order number
     */
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Build paged response from page of orders
     */
    private PagedResponse<OrderDTO> buildPagedResponse(Page<Order> orders) {
        return PagedResponse.<OrderDTO>builder()
                .content(orders.getContent().stream()
                        .map(orderMapper::toOrderDTO)
                        .collect(Collectors.toList()))
                .pageNumber(orders.getNumber())
                .pageSize(orders.getSize())
                .totalElements(orders.getTotalElements())
                .totalPages(orders.getTotalPages())
                .isLast(orders.isLast())
                .build();
    }
}

