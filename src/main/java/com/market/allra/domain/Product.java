package com.market.allra.domain;

import com.market.allra.domain.enums.StockStatus;
import com.market.allra.domain.enums.YesNo;
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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "products")
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 300)
    private String name;
    private int price;
    private int stock;
    @Enumerated(EnumType.STRING)
    private StockStatus status;
    @Enumerated(EnumType.STRING)
    private YesNo deleteYN = YesNo.N;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String name, int price, int stock, StockStatus status, Category category) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.category = category;
    }

    public Product(String name, int price, int stock, StockStatus status, Category category, YesNo deleteYN) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.category = category;
        this.deleteYN = deleteYN;
    }

    /* 비즈니스 메서드 */

    public boolean hasEnoughStock(int quantity) {
        return stock - quantity >= 0;
    }
}
