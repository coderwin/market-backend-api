package com.market.allra.domain;

import com.market.allra.domain.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 100)
    private String address;
    @Column(nullable = false)
    private Integer totalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Member member;
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST})
    private List<OrderProduct> orderProductList = new ArrayList<>();
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST})
    private List<Payment> paymentList = new ArrayList<>();

    /* 비즈니스 로직 */

    // 연관관계 편의 메서드
    public void addOrderProduct(OrderProduct orderProduct) {
        orderProduct.setOrder(this);
        orderProductList.add(orderProduct);
    }

    public void addPayment(Payment payment) {
        payment.setOrder(this);
        paymentList.add(payment);
    }
}
