package com.market.allra.service;

import com.market.allra.web.dto.AddBasketProductRequestDTO;
import com.market.allra.web.dto.BasketProductResponseDTO;
import com.market.allra.web.dto.UpdateBasketProductRequestDTO;
import com.market.allra.web.dto.UpdateBasketProductResponseDTO;

public interface BasketProductService {
    BasketProductResponseDTO addProductToBasket(Long basketId, Long memberId, AddBasketProductRequestDTO requestDTO);

    UpdateBasketProductResponseDTO updateProductToBasket(Long basketId, Long productId, Long memberId, UpdateBasketProductRequestDTO requestDTO);

    void deleteProductToBasket(Long basketId, Long productId, Long memberId);
}
