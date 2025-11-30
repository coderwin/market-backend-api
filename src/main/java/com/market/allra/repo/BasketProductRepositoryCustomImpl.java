package com.market.allra.repo;


import com.market.allra.domain.BasketProduct;
import com.market.allra.domain.enums.YesNo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.market.allra.domain.QBasket.basket;
import static com.market.allra.domain.QBasketProduct.basketProduct;
import static com.market.allra.domain.QProduct.product;

@RequiredArgsConstructor
public class BasketProductRepositoryCustomImpl implements BasketProductRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<BasketProduct> findByBasketIdAndDeleteYn(Long basketId, YesNo deleteYn) {
        return query.select(basketProduct)
                .from(basketProduct)
                .join(basketProduct.basket, basket)
                .join(basketProduct.product, product)
                .where(
                        basket.id.eq(basketId)
                        , product.deleteYN.eq(deleteYn)
                )
                .fetch();
    }
}

