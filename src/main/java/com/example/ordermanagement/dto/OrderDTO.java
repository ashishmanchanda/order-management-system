package com.example.ordermanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.ordermanagement.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Order response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long id;
    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String shippingAddress;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private List<OrderItemDTO> orderItems;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}

