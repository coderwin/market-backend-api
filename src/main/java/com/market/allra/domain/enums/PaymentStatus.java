package com.market.allra.domain.enums;

import lombok.Getter;

/**
 * 결제 상테
 */
@Getter
public enum PaymentStatus {
    PENDING("결제대기")
    , SUCCESS("결제성공")
    , FAILED("결제실패")
    , CANCELED("결제취소")
    , REFUNDED("결제환불")
    ;

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
