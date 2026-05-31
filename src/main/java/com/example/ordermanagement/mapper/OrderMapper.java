package com.example.ordermanagement.mapper;

import com.example.ordermanagement.dto.CreateOrderDTO;
import com.example.ordermanagement.dto.CreateOrderItemDTO;
import com.example.ordermanagement.dto.OrderDTO;
import com.example.ordermanagement.dto.OrderItemDTO;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for converting between Order entities and DTOs
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    /**
     * Convert Order entity to OrderDTO
     */
    OrderDTO toOrderDTO(Order order);

    /**
     * Convert OrderItem entity to OrderItemDTO
     */
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    /**
     * Convert CreateOrderDTO to Order entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    Order toOrder(CreateOrderDTO createOrderDTO);

    /**
     * Convert CreateOrderItemDTO to OrderItem entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrderItem toOrderItem(CreateOrderItemDTO createOrderItemDTO);
}

