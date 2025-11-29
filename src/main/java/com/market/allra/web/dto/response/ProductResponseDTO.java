package com.market.allra.web.dto.response;

import com.market.allra.domain.Product;
import com.market.allra.domain.enums.StockStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 상품 응답 DTO
 */
@Getter
public class ProductResponseDTO {
    private Long id;
    private String name;
    private int price;
    private int stock;
    private String status;
    private CategoryResponseDTO categoryResponseDTO;
    private LocalDateTime createdAt;

    @Builder
    public ProductResponseDTO(Long id, String name, int price, int stock, String status, CategoryResponseDTO categoryResponseDTO, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.categoryResponseDTO = categoryResponseDTO;
        this.createdAt = createdAt;
    }

    /* 비즈니스 로직 */
    public static ProductResponseDTO create(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus().getDescription())
                .createdAt(product.getCreatedAt())
                .categoryResponseDTO(CategoryResponseDTO.create(product.getCategory()))
                .build();
    }
}
