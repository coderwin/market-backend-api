package com.market.allra.web.dto.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드
 */
@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_INPUT_VALUE", "요청한 값이 올바르지 않습니다.")
    , INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.")

    , BASKET_NOT_FOUND(HttpStatus.BAD_REQUEST, "BASKET_NOT_FOUND", "장바구니를 찾을 수 없습니다.")
    , BASKET_ACCESS_DENIED(HttpStatus.FORBIDDEN, "BASKET_ACCESS_DENIED", "해당 장바구니에 접근할 수 없습니다.")
    , BASKET_INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "BASKET_ACCESS_DENIED", "상품 수량은 1 이상이어야 합니다.")

    , PRODUCT_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "PRODUCT_OUT_OF_STOCK", "재고가 부족합니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String defaultMessage;

    ErrorCode(HttpStatus httpStatus, String code, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
