package com.market.allra.service;

import com.market.allra.domain.Order;
import com.market.allra.domain.Payment;
import com.market.allra.web.dto.CheckoutResponseDTO;
import com.market.allra.web.dto.CheckoutRequestDTO;
import com.market.allra.web.dto.OrderResponseDTO;
import com.market.allra.web.dto.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckOutServiceImpl implements CheckoutService{

    private final OrderService orderService;
    private final PaymentService paymentService;

    @Transactional
    @Override
    public CheckoutResponseDTO checkout(CheckoutRequestDTO checkoutRequestDTO, Long memberId) {
        // 주문 생성
        Order order = orderService.createOrder(checkoutRequestDTO.getBasketId(), memberId);

        // 결제 생성(결제 시도)
        Payment payment = paymentService.createPayment(order, checkoutRequestDTO.getPaymentMethod());

        // DTO 생성
        OrderResponseDTO orderResponseDTO = OrderResponseDTO.create(order);
        PaymentResponseDTO paymentResponseDTO = PaymentResponseDTO.create(payment);

        return CheckoutResponseDTO.create(orderResponseDTO, paymentResponseDTO);
    }
}
