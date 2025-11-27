package com.market.allra.domain.enums;

import lombok.Getter;

/**
 * 주문 상태
 */
@Getter
public enum OrderStatus {
    CREATED("주문 생성")
    , PAYMENT_PENDING("결제 대기")
    , PAYMENT_COMPLETED("결제 완료")
    , PAYMENT_CANCELED("주문 취소")
    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

}
