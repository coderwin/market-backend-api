package com.market.allra.service;

import com.market.allra.domain.Basket;
import com.market.allra.domain.Order;
import com.market.allra.domain.OrderProduct;
import com.market.allra.domain.Product;
import com.market.allra.domain.enums.StockStatus;
import com.market.allra.domain.enums.YesNo;
import com.market.allra.exception.BusinessException;
import com.market.allra.repo.BasketRepository;
import com.market.allra.repo.OrderRepository;
import com.market.allra.repo.ProductRepository;
import com.market.allra.web.dto.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{

    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public Order createOrder(Long basketId, Long memberId) {
        // 장바구니 찾기 with 회원id
        Basket findBasket = basketRepository.findByIdAndMemberId(basketId, memberId).orElseThrow(
                () -> new BusinessException(ErrorCode.BASKET_NOT_FOUND)
        );

        // 재고 체크
        List<OrderProduct> orderProductList = findBasket.getBasketProdcutList().stream()
                .filter(basketProduct ->
                        basketProduct.getProduct().getDeleteYN().equals(YesNo.N)
                        && basketProduct.getProduct().getStatus().equals(StockStatus.IN_STOCK)
                )
                .map(basketProduct -> {
                    Product product = basketProduct.getProduct();
                    // 재고 체크
                    if(!product.hasEnoughStock(basketProduct.getQuantity())) {
                        throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
                    };
                    // 재고 차감
                    // 동시성 처리
                    // 재고 상태 변경 확인
                    productRepository.updateDecreaseStock(product.getId(), basketProduct.getQuantity());
                    // OrderProduct 생성
                    return OrderProduct.create(basketProduct);
                }).toList();

        // 전체합
        int totalPrice = orderProductList.stream()
                .mapToInt(OrderProduct::getTotalPrice)
                .sum();

        // 주문 생성
        final Order order = Order.create(findBasket.getMember(), totalPrice);

        // 연관관계
        findBasket.getMember().addOrder(order);
        orderProductList.forEach(order::addOrderProduct);

        orderRepository.save(order);

        return order;
    }
}
