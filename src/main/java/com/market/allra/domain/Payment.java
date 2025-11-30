package com.market.allra.domain;

import com.market.allra.domain.enums.PaymentMethod;
import com.market.allra.domain.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Comment(value = "결제 테이블")
@Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 100)
    private String transactionId;
    private int amount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime paidAt;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    /* 비즈니스 로직 */

    public static Payment create(Order order, PaymentMethod paymentMethod) {
        Payment payment = new Payment();

        payment.amount = order.getTotalPrice();
        payment.paymentMethod = paymentMethod;
        payment.paymentStatus = PaymentStatus.PENDING;

        return payment;
    }

    public void changeStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void addTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
