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

        // 기존 장바구니에 같은 상품이 있으면
        BasketProduct presentBasketProduct = getPresentBasketProductFromBasket(findBasket, findProduct);

        // 수량 생성
        int newQuantity = presentBasketProduct == null
                ? requestDTO.getQuantity()
                : presentBasketProduct.getQuantity() + requestDTO.getQuantity();

        // 상품 재고 확인
        if(!findProduct.hasEnoughStock(newQuantity)) {
            throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
        }

        // 장바구니-상품 존재 여부 확인
        BasketProduct responseBasketProduct = null;
        if(presentBasketProduct == null) {
            responseBasketProduct = BasketProduct.create(requestDTO.getQuantity(), findBasket, findProduct);
            // 결과값으로 basket_product_id를 보여주기 위해서 save 사용
            // findBasket.addBasketProduct(responseBasketProduct);
            basketProductRepository.save(responseBasketProduct);
        } else {
            presentBasketProduct.changeQuantity(newQuantity);
            responseBasketProduct = presentBasketProduct;
        }

        return BasketProductResponseDTO.create(responseBasketProduct);
    }

    private static BasketProduct getPresentBasketProductFromBasket(Basket findBasket, Product findProduct) {
        return findBasket.getBasketProdcutList().stream()
                .filter((data) -> data.getProduct().getId().equals(findProduct.getId()))
                .findFirst()
                .orElse(null);
    }
}
