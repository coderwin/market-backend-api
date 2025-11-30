package com.market.allra.repo;

import com.market.allra.domain.BasketProduct;
import com.market.allra.domain.enums.YesNo;

import java.util.List;

public interface BasketProductRepositoryCustom {
    List<BasketProduct> findByBasketIdAndDeleteYn(Long basketId, YesNo deleteYn);
}
