package com.example.ordermanagement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for creating a new Order
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderDTO {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Customer email should be valid")
    private String customerEmail;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<CreateOrderItemDTO> orderItems;
}

