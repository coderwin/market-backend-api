package com.market.allra.web.dto;

import com.market.allra.domain.enums.PaymentMethod;
import lombok.Getter;

/**
 * 주문 및 결제 요청 DTO
 */
@Getter
public class CheckoutRequestDTO {
    private Long basketId;
    private PaymentMethod paymentMethod;
}
