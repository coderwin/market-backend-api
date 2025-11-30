package com.market.allra.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_order_id_product_id"
                        , columnNames = {"order_id", "product_id"}
                )
        }
)
@Getter
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

    @Builder
    public OrderProduct(Long id, int quantity, int priceAtOrder, Order order, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
        this.order = order;
        this.product = product;
    }

    /* 비즈니스 메서드 */

    public static OrderProduct create(BasketProduct basketProduct) {
        return OrderProduct.builder()
                .quantity(basketProduct.getQuantity())
                .priceAtOrder(basketProduct.getProduct().getPrice())
                .product(basketProduct.getProduct())
                .build();
    }

    public int getTotalPrice() {
        return priceAtOrder * quantity;
    }
}
