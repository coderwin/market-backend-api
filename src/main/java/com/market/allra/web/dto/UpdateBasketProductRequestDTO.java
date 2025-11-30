package com.market.allra.web.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 장바구니-상품 수정 DTO
 */
@Getter
public class UpdateBasketProductRequestDTO {
    private int quantity;

    @Builder
    public UpdateBasketProductRequestDTO(int quantity) {
        this.quantity = quantity;
    }

    /* 비즈니스 로직 */

    public static UpdateBasketProductRequestDTO of(int quantity) {
        return UpdateBasketProductRequestDTO.builder()
                .quantity(quantity)
                .build();
    }
}
