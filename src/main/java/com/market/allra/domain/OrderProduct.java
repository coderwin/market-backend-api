package com.market.allra.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class OrderProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    @Comment(value = "주문당시 가격(기록용)")
    private int priceAtOrder;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

}
