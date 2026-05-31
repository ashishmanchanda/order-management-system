package com.example.ordermanagement.controller;

import com.example.ordermanagement.dto.CreateOrderDTO;
import com.example.ordermanagement.dto.OrderDTO;
import com.example.ordermanagement.dto.PagedResponse;
import com.example.ordermanagement.entity.OrderStatus;
import com.example.ordermanagement.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Order management endpoints
 */
@RestController
@RequestMapping("/orders")
@Tag(name = "Order Management", description = "APIs for managing orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Create a new order
     */
    @PostMapping
    @Operation(summary = "Create a new order", description = "Create a new order with multiple items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderDTO createOrderDTO) {
        log.info("Creating new order for customer: {}", createOrderDTO.getCustomerName());
        OrderDTO orderDTO = orderService.createOrder(createOrderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }

    /**
     * Get order by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve order details by order ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<OrderDTO> getOrderById(
            @Parameter(description = "Order ID", example = "1")
            @PathVariable Long id) {
        log.info("Fetching order with ID: {}", id);
        OrderDTO orderDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDTO);
    }

    /**
     * Get order by order number
     */
    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by order number", description = "Retrieve order details by order number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<OrderDTO> getOrderByOrderNumber(
            @Parameter(description = "Order Number", example = "ORD-123456789-ABCD")
            @PathVariable String orderNumber) {
        log.info("Fetching order with order number: {}", orderNumber);
        OrderDTO orderDTO = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(orderDTO);
    }

    /**
     * Get all orders with pagination
     */
    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve all orders with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PagedResponse<OrderDTO>> getAllOrders(
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int pageNumber,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("Fetching all orders - page: {}, size: {}", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        PagedResponse<OrderDTO> response = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get orders by status with pagination
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Retrieve orders filtered by status with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PagedResponse<OrderDTO>> getOrdersByStatus(
            @Parameter(description = "Order Status")
            @PathVariable OrderStatus status,
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int pageNumber,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("Fetching orders with status: {} - page: {}, size: {}", status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        PagedResponse<OrderDTO> response = orderService.getOrdersByStatus(status, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get orders by customer email with pagination
     */
    @GetMapping("/customer/{customerEmail}")
    @Operation(summary = "Get orders by customer email", description = "Retrieve orders for a specific customer with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PagedResponse<OrderDTO>> getOrdersByCustomerEmail(
            @Parameter(description = "Customer Email")
            @PathVariable String customerEmail,
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int pageNumber,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("Fetching orders for customer: {} - page: {}, size: {}", customerEmail, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        PagedResponse<OrderDTO> response = orderService.getOrdersByCustomerEmail(customerEmail, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Update order status
     */
    @PutMapping("/{id}/status/{status}")
    @Operation(summary = "Update order status", description = "Update the status of an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @Parameter(description = "Order ID")
            @PathVariable Long id,
            @Parameter(description = "New Order Status")
            @PathVariable OrderStatus status) {
        log.info("Updating order status - ID: {}, new status: {}", id, status);
        OrderDTO orderDTO = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(orderDTO);
    }

    /**
     * Cancel an order
     */
    @DeleteMapping("/{id}/cancel")
    @Operation(summary = "Cancel an order", description = "Cancel an order (only if it's in PENDING status)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Order cannot be cancelled (not in PENDING status)"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<OrderDTO> cancelOrder(
            @Parameter(description = "Order ID")
            @PathVariable Long id) {
        log.info("Cancelling order with ID: {}", id);
        OrderDTO orderDTO = orderService.cancelOrder(id);
        return ResponseEntity.ok(orderDTO);
    }

    /**
     * Get order count by status
     */
    @GetMapping("/count/status/{status}")
    @Operation(summary = "Get order count by status", description = "Get the count of orders for a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> getOrderCountByStatus(
            @Parameter(description = "Order Status")
            @PathVariable OrderStatus status) {
        log.info("Getting count of orders with status: {}", status);
        long count = orderService.getOrderCountByStatus(status);
        return ResponseEntity.ok(count);
    }
}

