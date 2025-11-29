package com.market.allra.exception;

import com.market.allra.web.dto.exception.ErrorCode;
import lombok.Getter;

/**
 * 비즈니스 예외
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
