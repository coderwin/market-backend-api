package com.market.allra.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "basket")
@Comment(value = "장바구니 테이블")
public class Basket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "basket", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<BasketProduct> basketProdcutList = new ArrayList<>();

    @Builder
    public Basket(Long id, Member member) {
        this.id = id;
        this.member = member;
    }

    /* 비즈니스 로직 */

    // 연관관계 편의 메서드
    public void addBasketProduct(BasketProduct basketProduct) {
        basketProduct.setBasket(this);
        basketProdcutList.add(basketProduct);
    }

}
