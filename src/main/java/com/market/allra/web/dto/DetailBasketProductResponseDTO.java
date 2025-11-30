package com.market.allra.web.dto;

import com.market.allra.domain.BasketProduct;
import com.market.allra.web.dto.response.ProductResponseDTO;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 장바구니_상품 상세 DTO
 */
@Getter
public class DetailBasketProductResponseDTO {
    private Long basketProductId;
    private int quantity;
    private LocalDateTime basketProductCreatedAt;

    private ProductResponseDTO productResponseDTO;

    @Builder
    public DetailBasketProductResponseDTO(Long basketProductId, int quantity, LocalDateTime basketProductCreatedAt, ProductResponseDTO productResponseDTO) {
        this.basketProductId = basketProductId;
        this.quantity = quantity;
        this.basketProductCreatedAt = basketProductCreatedAt;
        this.productResponseDTO = productResponseDTO;
    }

    /* 비즈니스 로직 */

    public static DetailBasketProductResponseDTO create(BasketProduct basketProduct) {
        return DetailBasketProductResponseDTO.builder()
                .basketProductId(basketProduct.getId())
                .quantity(basketProduct.getQuantity())
                .basketProductCreatedAt(basketProduct.getCreatedAt())
                .productResponseDTO(ProductResponseDTO.create(basketProduct.getProduct()))
                .build();
    }
}
