package com.market.allra.service;

import com.market.allra.domain.Basket;
import com.market.allra.domain.BasketProduct;
import com.market.allra.domain.Product;
import com.market.allra.exception.BusinessException;
import com.market.allra.repo.BasketProductRepository;
import com.market.allra.repo.BasketRepository;
import com.market.allra.repo.ProductRepository;
import com.market.allra.web.dto.AddBasketProductRequestDTO;
import com.market.allra.web.dto.BasketProductResponseDTO;
import com.market.allra.web.dto.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasketProductServiceImpl implements BasketProductService {

    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final BasketProductRepository basketProductRepository;

    @Transactional
    @Override
    public BasketProductResponseDTO addProductToBasket(Long basketId, Long memberId, AddBasketProductRequestDTO requestDTO) {
        // 사용자 장바구니 찾기
        Basket findBasket = basketRepository.findByIdAndMemberId(basketId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BASKET_NOT_FOUND));

        // 상품 찾기
        Product findProduct = productRepository.findById(requestDTO.getProductId()).orElseThrow(
                () -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND)
        );

        // 상품 재고 확인
        if(!findProduct.hasEnoughStock(requestDTO.getQuantity())) {
            throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
        }

        // 장바구니에 상품 등록
        BasketProduct basketProduct = BasketProduct.create(requestDTO.getQuantity(), findBasket, findProduct);
        basketProductRepository.save(basketProduct);

        return BasketProductResponseDTO.create(basketProduct);
    }
}
