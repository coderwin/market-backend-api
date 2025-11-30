package com.market.allra.service;

import com.market.allra.web.dto.CheckoutResponseDTO;
import com.market.allra.web.dto.CheckoutRequestDTO;

/**
 * 주문 및 결제 처리 service
 */
public interface CheckoutService {
    CheckoutResponseDTO checkout(CheckoutRequestDTO checkoutRequestDTO, Long memberId);
}
