package com.market.allra.web.dto;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

/**
 * 장바구니-상품 추가 DTO
 */
@Getter
public class AddBasketProductRequestDTO {
    private Long productId;         // 상푸ID
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private int quantity;           // 수량

    @Builder
    public AddBasketProductRequestDTO(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    /* 비즈니스 메서드 */

    public static AddBasketProductRequestDTO of(Long productId, int quantity) {
        return AddBasketProductRequestDTO.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
