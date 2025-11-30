package com.market.allra.web.controller;

import com.market.allra.service.BasketProductService;
import com.market.allra.web.dto.AddBasketProductRequestDTO;
import com.market.allra.web.dto.BasketProductResponseDTO;
import com.market.allra.web.dto.LoginMemberResponseDTO;
import com.market.allra.web.dto.SessionKeys;
import com.market.allra.web.dto.UpdateBasketProductRequestDTO;
import com.market.allra.web.dto.UpdateBasketProductResponseDTO;
import com.market.allra.web.dto.response.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RequestMapping("/api/v1/baskets/{basketId}/products")
@RestController
@RequiredArgsConstructor
@Slf4j
public class BasketProductController {

    private final BasketProductService basketProductService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<BasketProductResponseDTO>> addProductToBasket(
            @RequestBody @Validated AddBasketProductRequestDTO requestDTO,
            @PathVariable Long basketId,
            @SessionAttribute(SessionKeys.LOGIN_MEMBER) LoginMemberResponseDTO loginMemberDTO
    ) {
        System.out.println(loginMemberDTO.getId());
        System.out.println(loginMemberDTO.getEmail());

        return ResponseEntity.ok(
                ApiResponseDTO.success(basketProductService.addProductToBasket(basketId, loginMemberDTO.getId(), requestDTO))
        );
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ApiResponseDTO<UpdateBasketProductResponseDTO>> updateProductToBasket(
            @RequestBody @Validated UpdateBasketProductRequestDTO requestDTO
            , @PathVariable Long basketId
            , @PathVariable Long productId
            , @SessionAttribute(SessionKeys.LOGIN_MEMBER) LoginMemberResponseDTO loginMemberDTO
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(basketProductService.updateProductToBasket(basketId, productId, loginMemberDTO.getId(), requestDTO))
        );
    }


}
