package com.market.allra.service;

import com.market.allra.web.dto.AddBasketProductRequestDTO;
import com.market.allra.web.dto.BasketProductResponseDTO;

public interface BasketProductService {
    BasketProductResponseDTO addProductToBasket(Long basketId, Long memberId, AddBasketProductRequestDTO requestDTO);
}
