~/package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.CreateOrderDTO;
import com.example.ordermanagement.dto.CreateOrderItemDTO;
import com.example.ordermanagement.dto.OrderDTO;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.OrderStatus;
import com.example.ordermanagement.exception.OrderCancellationException;
import com.example.ordermanagement.exception.OrderNotFoundException;
import com.example.ordermanagement.mapper.OrderMapper;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for OrderService
 */
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    public void testCreateOrder() {
        CreateOrderDTO createOrderDTO = CreateOrderDTO.builder()
                .customerName("John Doe")
                .customerEmail("john@example.com")
                .shippingAddress("123 Main St")
                .orderItems(List.of(
                        CreateOrderItemDTO.builder()
                                .productName("Product 1")
                                .productCode("PROD-001")
                                .quantity(2)
                                .unitPrice(BigDecimal.valueOf(50.00))
                                .build()
                ))
                .build();

        OrderDTO createdOrder = orderService.createOrder(createOrderDTO);

        assertNotNull(createdOrder.getId());
        assertEquals("John Doe", createdOrder.getCustomerName());
        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        assertEquals(1, createdOrder.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(100.00), createdOrder.getTotalAmount());
    }

    @Test
    public void testGetOrderById() {
        Order order = Order.builder()
                .orderNumber("ORD-TEST-001")
                .customerName("John Doe")
                .customerEmail("john@example.com")
                .shippingAddress("123 Main St")
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.valueOf(100.00))
                .build();
        Order savedOrder = orderRepository.save(order);

        OrderDTO retrievedOrder = orderService.getOrderById(savedOrder.getId());

        assertNotNull(retrievedOrder);
        assertEquals("John Doe", retrievedOrder.getCustomerName());
    }

    @Test
    public void testGetOrderById_NotFound() {
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(999L));
    }

    @Test
    public void testCancelOrder_Success() {
        Order order = Order.builder()
                .orderNumber("ORD-TEST-001")
                .customerName("John Doe")
                .customerEmail("john@example.com")
                .shippingAddress("123 Main St")
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.valueOf(100.00))
                .build();
        Order savedOrder = orderRepository.save(order);

        OrderDTO cancelledOrder = orderService.cancelOrder(savedOrder.getId());

        assertEquals(OrderStatus.CANCELLED, cancelledOrder.getStatus());
    }

    @Test
    public void testCancelOrder_NotPending() {
        Order order = Order.builder()
                .orderNumber("ORD-TEST-001")
                .customerName("John Doe")
                .customerEmail("john@example.com")
                .shippingAddress("123 Main St")
                .status(OrderStatus.PROCESSING)
                .totalAmount(BigDecimal.valueOf(100.00))
                .build();
        Order savedOrder = orderRepository.save(order);

        assertThrows(OrderCancellationException.class, () -> orderService.cancelOrder(savedOrder.getId()));
    }

    @Test
    public void testUpdateOrderStatus() {
        Order order = Order.builder()
                .orderNumber("ORD-TEST-001")
                .customerName("John Doe")
                .customerEmail("john@example.com")
                .shippingAddress("123 Main St")
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.valueOf(100.00))
                .build();
        Order savedOrder = orderRepository.save(order);

        OrderDTO updatedOrder = orderService.updateOrderStatus(savedOrder.getId(), OrderStatus.PROCESSING);

        assertEquals(OrderStatus.PROCESSING, updatedOrder.getStatus());
    }
}

