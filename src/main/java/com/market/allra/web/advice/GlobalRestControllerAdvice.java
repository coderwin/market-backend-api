package com.market.allra.web.advice;

import com.market.allra.exception.BusinessException;
import com.market.allra.web.dto.exception.ErrorCode;
import com.market.allra.web.dto.response.ApiResponseDTO;
import com.market.allra.web.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전체 예외 처리 restControllerAdvice
 */
@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleMethodArgumentNotValidEx(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse(ErrorCode.INVALID_INPUT_VALUE.getDefaultMessage());

        ErrorResponseDTO response = ErrorResponseDTO.of(
                ErrorCode.INVALID_INPUT_VALUE.getCode()
                , message
                , ErrorCode.INVALID_INPUT_VALUE.getHttpStatus().value()
                , request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.failure(response));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleConstraintViolationEx(ConstraintViolationException ex, HttpServletRequest request) {
        String message = ex.getConstraintViolations().stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse(ErrorCode.INVALID_INPUT_VALUE.getDefaultMessage());

        ErrorResponseDTO response = ErrorResponseDTO.of(
                ErrorCode.INVALID_INPUT_VALUE.getCode()
                , message
                , ErrorCode.INVALID_INPUT_VALUE.getHttpStatus().value()
                , request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.failure(response));

    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleException(Exception ex, HttpServletRequest request) {
        ErrorResponseDTO response = ErrorResponseDTO.of(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode()
                , ErrorCode.INTERNAL_SERVER_ERROR.getDefaultMessage()
                , ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value()
                , request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.failure(response));
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleBusinessEx(BusinessException ex, HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();

        ErrorResponseDTO response = ErrorResponseDTO.of(
                errorCode.getCode()
                , ex.getMessage()
                , errorCode.getHttpStatus().value()
                , request.getRequestURI()
        );

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponseDTO.failure(response));
    }
}
