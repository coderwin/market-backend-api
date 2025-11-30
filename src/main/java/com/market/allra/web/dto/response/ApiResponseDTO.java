package com.market.allra.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

/**
 * 응답 DTO
 */
@Getter
public class ApiResponseDTO<T> {
    private final boolean success;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final ErrorResponseDTO error;

    public ApiResponseDTO(boolean success, T data, ErrorResponseDTO error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    /* 비즈니스 로직 */
    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<> (true, data, null);
    }

    public static <T> ApiResponseDTO<T> failure(ErrorResponseDTO error) {
        return new ApiResponseDTO<> (false, null, error);
    }
}
