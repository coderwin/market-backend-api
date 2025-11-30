package com.market.allra.service;

import com.market.allra.web.dto.AddBasketProductRequestDTO;
import com.market.allra.web.dto.BasketProductResponseDTO;
import com.market.allra.web.dto.DetailBasketProductResponseDTO;
import com.market.allra.web.dto.UpdateBasketProductRequestDTO;
import com.market.allra.web.dto.UpdateBasketProductResponseDTO;

import java.util.List;

public interface BasketProductService {
    BasketProductResponseDTO addProductToBasket(Long basketId, Long memberId, AddBasketProductRequestDTO requestDTO);

    UpdateBasketProductResponseDTO updateProductToBasket(Long basketId, Long productId, Long memberId, UpdateBasketProductRequestDTO requestDTO);

    void deleteProductToBasket(Long basketId, Long productId, Long memberId);

    List<DetailBasketProductResponseDTO> search(Long basketId, Long memberId);
}
