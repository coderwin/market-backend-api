package com.market.allra.web.dto;

import com.market.allra.domain.BasketProduct;
import lombok.Builder;
import lombok.Getter;

/**
 * 장바구니-상품 응답 DTO
 */
@Getter
public class BasketProductResponseDTO {
    private Long basket_product_id;
    private int quantity;

    @Builder
    public BasketProductResponseDTO(Long basket_product_id, int quantity) {
        this.basket_product_id = basket_product_id;
        this.quantity = quantity;
    }

    /* 비즈니스 로직 */

    public static BasketProductResponseDTO create(BasketProduct basketProduct) {
        return BasketProductResponseDTO.builder()
                .basket_product_id(basketProduct.getId())
                .quantity(basketProduct.getQuantity())
                .build();
    }
}
