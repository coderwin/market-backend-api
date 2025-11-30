package com.market.allra.web.dto;

import com.market.allra.web.dto.enums.ProcessStatus;
import lombok.Getter;

/**
 * 결제 응답 DTO
 */
@Getter
public class PayResponseDTO {
    private ProcessStatus status;
    private String transactionId;
    private String message;
}
