package com.market.allra.web.dto;

import com.market.allra.domain.BasketProduct;
import lombok.Builder;
import lombok.Getter;

/**
 * 장바구니-상품 응답 DTO
 */
@Getter
public class BasketProductResponseDTO {
    private Long id;
    private int quantity;

    @Builder
    public BasketProductResponseDTO(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    /* 비즈니스 로직 */

    public static BasketProductResponseDTO create(BasketProduct basketProduct) {
        return BasketProductResponseDTO.builder()
                .id(basketProduct.getId())
                .quantity(basketProduct.getQuantity())
                .build();
    }
}
