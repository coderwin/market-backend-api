package com.market.allra.web.dto;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

/**
 * 장바구니-상품 수정 DTO
 */
@Getter
public class UpdateBasketProductRequestDTO {
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
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
