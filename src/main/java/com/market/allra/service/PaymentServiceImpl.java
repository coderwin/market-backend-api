package com.market.allra.service;

import com.market.allra.domain.Order;
import com.market.allra.domain.Payment;
import com.market.allra.domain.Product;
import com.market.allra.domain.enums.OrderStatus;
import com.market.allra.domain.enums.PaymentMethod;
import com.market.allra.domain.enums.PaymentStatus;
import com.market.allra.exception.BusinessException;
import com.market.allra.repo.PaymentRepository;
import com.market.allra.repo.ProductRepository;
import com.market.allra.web.dto.PayRequestDTO;
import com.market.allra.web.dto.PayResponseDTO;
import com.market.allra.web.dto.enums.ProcessStatus;
import com.market.allra.web.dto.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    @Override
    public Payment createPayment(Order order, PaymentMethod paymentMethod) {
        // order null이면 예외 발생
        if(order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }

        // 결제 생성
        // 결제-대기
        Payment payment = Payment.create(order, paymentMethod);
        order.addPayment(payment);

        // 결제 시도
        // 주문-결제 대기
        order.changeStatus(OrderStatus.PAYMENT_PENDING);

        try {
            PayResponseDTO payResponseDTO = pay(order.getId(), order.getTotalPrice());

            // 트랜젝션 아이디 추가
            payment.addTransactionId(payResponseDTO.getTransactionId());

            // 결제 성공
            if (payResponseDTO.getStatus().equals(ProcessStatus.SUCCESS)) {
                // 결제-성공
                order.changeStatus(OrderStatus.PAYMENT_COMPLETED);
                payment.changeStatus(PaymentStatus.SUCCESS);
                // 결제 실패
            } else {
                canceled(order, payment);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            canceled(order, payment);
        }

        paymentRepository.save(payment);

        return payment;
    }

    private void canceled(Order order, Payment payment) {
        // 주문 취소
        order.changeStatus(OrderStatus.CANCELED);
        payment.changeStatus(PaymentStatus.FAILED);
        // 재고 복원
        order.getOrderProductList().forEach(orderProduct -> {
            Product product = orderProduct.getProduct();
            productRepository.updateIncreaseStock(product.getId(), orderProduct.getQuantity());
        });
    }

    // 외부 결제 시도
    private PayResponseDTO pay(Long orderId, int totalPrice) {
        String uri = "https://market.free.beeceptor.com/api/v1/payment";

        WebClient webClient = WebClient.create();

        PayRequestDTO requestDTO = PayRequestDTO.of(orderId, totalPrice);

        return webClient.post()
                .uri(uri)
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(PayResponseDTO.class)
                .block();
    }
}
