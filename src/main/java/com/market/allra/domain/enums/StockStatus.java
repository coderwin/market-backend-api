package com.market.allra.domain.enums;

import lombok.Getter;

/**
 * 재고 상태
 */
@Getter
public enum StockStatus {
    IN_STOCK("재고 있음")
    , SOLD_OUT("품절")
    ;

    private final String description;

    StockStatus(String description) {
        this.description = description;
    }
}
