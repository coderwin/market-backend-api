package com.market.allra.web.dto;

import lombok.Getter;

/**
 * 결제 요청 DTO
 */
@Getter
public class PayRequestDTO {
    private Long orderId;
    private int amount;

    /* 비즈니스 로직 */

    public static PayRequestDTO of(Long orderId, int amount) {
        PayRequestDTO payRequestDTO = new PayRequestDTO();

        payRequestDTO.orderId = orderId;
        payRequestDTO.amount = amount;

        return payRequestDTO;
    }
}
