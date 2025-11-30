package com.market.allra.web.controller;

import com.market.allra.service.CheckoutService;
import com.market.allra.web.dto.CheckoutRequestDTO;
import com.market.allra.web.dto.CheckoutResponseDTO;
import com.market.allra.web.dto.LoginMemberResponseDTO;
import com.market.allra.web.dto.SessionKeys;
import com.market.allra.web.dto.response.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RequestMapping("/api/v1/checkout")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<CheckoutResponseDTO>> checkout(
            @RequestBody @Validated CheckoutRequestDTO requestDTO,
            @SessionAttribute(SessionKeys.LOGIN_MEMBER) LoginMemberResponseDTO loginMemberDTO
    ) {

        return ResponseEntity.ok(
            ApiResponseDTO.success(checkoutService.checkout(requestDTO, loginMemberDTO.getId()))
        );
    }
}
