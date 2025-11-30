package com.market.allra.domain;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "basket_product"
        , uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_basket_id_product_id"
                        , columnNames = {"basket_id", "product_id"}
                )
        }
)
@Getter
public class BasketProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    @CreatedDate
    private LocalDateTime createdAt;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /* 비즈니스 로직 */

    public static BasketProduct create(int quantity, Basket basket, Product product) {
        BasketProduct basketProduct = new BasketProduct();

        basketProduct.quantity = quantity;
        basketProduct.product = product;
        basket.addBasketProduct(basketProduct);

        return basketProduct;
    }

    public void changeQuantity(int newQuantity) {
        quantity = newQuantity;
    }
}


