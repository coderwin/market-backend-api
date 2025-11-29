package com.market.allra.web.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class ErrorResponseDTO {
    private final String code;
    private final String message;
    private final int status;
    private final String path;              // 요청 URI
    private final LocalDateTime timestamp;

    public static ErrorResponseDTO of(String code, String message, int status, String path) {
        return  new ErrorResponseDTO(code, message, status, path, LocalDateTime.now());
    }
}
