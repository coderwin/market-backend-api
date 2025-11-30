package com.market.allra.web.dto;

import com.market.allra.domain.Order;
import com.market.allra.domain.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 주문 응답 DTO
 */
@Getter
public class OrderResponseDTO {
    private Long id;
    private String name;
    private String address;
    private int totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;

    @Builder
    public OrderResponseDTO(Long id, String name, String address, int totalPrice, OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    /* 비즈니스 로직 */

    public static OrderResponseDTO create(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .name(order.getName())
                .address(order.getAddress())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
