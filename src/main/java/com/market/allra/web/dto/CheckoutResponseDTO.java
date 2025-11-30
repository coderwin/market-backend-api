package com.market.allra.web.dto;

import lombok.Getter;

/**
 * 주문 및 결제 응답 DTO
 */
@Getter
public class CheckoutResponseDTO {
    private OrderResponseDTO orderResponseDTO;
    private PaymentResponseDTO paymentResponseDTO;

    /* 비즈니스 로직 */

    public static CheckoutResponseDTO create(OrderResponseDTO orderResponseDTO, PaymentResponseDTO paymentResponseDTO) {
        CheckoutResponseDTO checkOutResponseDTO = new CheckoutResponseDTO();

        checkOutResponseDTO.orderResponseDTO = orderResponseDTO;
        checkOutResponseDTO.paymentResponseDTO = paymentResponseDTO;

        return checkOutResponseDTO;
    }
}
