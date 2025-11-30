package com.market.allra.web.dto;

import com.market.allra.domain.Payment;
import com.market.allra.domain.enums.PaymentMethod;
import com.market.allra.domain.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

/**
 * 결제 응답 DTO
 */
@Getter
public class PaymentResponseDTO {
    private Long id;
    private String transactionId;
    private int amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paidAt;

    @Builder
    public PaymentResponseDTO(Long id, String transactionId, int amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus, LocalDateTime paidAt) {
        this.id = id;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paidAt = paidAt;
    }

    /* 비즈니스 로직 */

    public static PaymentResponseDTO create(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .paidAt(payment.getPaidAt())
                .build();
    }
}
