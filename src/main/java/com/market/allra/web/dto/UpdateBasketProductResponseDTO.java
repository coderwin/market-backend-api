package com.market.allra.web.dto;

import com.market.allra.domain.BasketProduct;
import lombok.Builder;
import lombok.Getter;

/**
 * 장바구니-상품 수정 DTO
 */
@Getter
public class UpdateBasketProductResponseDTO {
    private Long basketProductId;
    private Long basketId;
    private Long productId;
    private int quantity;

    @Builder

    public UpdateBasketProductResponseDTO(Long basketProductId, Long basketId, Long productId, int quantity) {
        this.basketProductId = basketProductId;
        this.basketId = basketId;
        this.productId = productId;
        this.quantity = quantity;
    }

    /* 비즈니스 로직 */

    public static UpdateBasketProductResponseDTO create(BasketProduct basketProduct) {
        return UpdateBasketProductResponseDTO.builder()
                .basketProductId(basketProduct.getId())
                .basketId(basketProduct.getBasket().getId())
                .productId(basketProduct.getProduct().getId())
                .quantity(basketProduct.getQuantity())
                .build();
    }
}
