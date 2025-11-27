package com.market.allra.domain.enums;

import lombok.Getter;

/**
 * 결제 방법
 */
@Getter
public enum PaymentMethod {
    CARD("카드 결제")
    , BANK_TRANSFER("계좌이체")
    , KAKAO("카카오 결제")
    , NAVER("네이버 결제")
    ;

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }
}
