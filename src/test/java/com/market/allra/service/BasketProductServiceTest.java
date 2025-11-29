package com.market.allra.service;

import com.market.allra.domain.Basket;
import com.market.allra.domain.Category;
import com.market.allra.domain.Member;
import com.market.allra.domain.Product;
import com.market.allra.domain.enums.StockStatus;
import com.market.allra.exception.BusinessException;
import com.market.allra.repo.BasketRepository;
import com.market.allra.repo.CategoryRepository;
import com.market.allra.repo.MemberRepository;
import com.market.allra.web.dto.AddBasketProductRequestDTO;
import com.market.allra.web.dto.BasketProductResponseDTO;
import com.market.allra.web.dto.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles(value = "test")
public class BasketProductServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    BasketProductService basketProductService;
    @Autowired
    CategoryRepository categoryRepository;

    private Member nowMember;
    private Basket basket;
    private Category categoryFruit;
    private Product product;

    @BeforeEach
    void init() {
        // 사용자 추가
        nowMember = Member.builder().name("A").email("test1@test.com").password("1234!@#").address("서울특별시 관악구").build();
        memberRepository.save(nowMember);

        // 장바구니 추가(사용자의 장바구니 아님)
        basket = Basket.builder().member(nowMember).build();
        basketRepository.save(basket);

        // 상품 추가
        product = new Product("사과", 1000, 1, StockStatus.IN_STOCK, categoryFruit);
        categoryFruit = new Category("과일");

        categoryFruit.addProduct(product);
        categoryRepository.save(categoryFruit);
    }

    @Test
    public void 사용자의_장바구니_찾는_중_없으면_예외가_발생한다() {
        // given
        // 다른 사용자 추가
        Member diffrentMember = Member.builder().name("B").email("test2@test.com").password("1234!@#").address("서울특별시 관악구").build();
        memberRepository.save(diffrentMember);

        // when // then
        // addProductToBasket 예외 발생
        int quantity = 10;
        AddBasketProductRequestDTO requestDTO = AddBasketProductRequestDTO.of(product.getId(), quantity);

        BusinessException ex = Assertions.assertThrows(BusinessException.class, () -> {
            basketProductService.addProductToBasket(basket.getId(), diffrentMember.getId(), requestDTO);
        });

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.BASKET_NOT_FOUND);
        assertThat(basket.getMember().getId()).isNotEqualTo(diffrentMember.getId());
    }

    @Test
    public void 사용자의_장바구니_찾는_중_있으면_예외가_발생_안_한다() {
        // given

        // when // then
        int quantity = 1;
        AddBasketProductRequestDTO requestDTO = AddBasketProductRequestDTO.of(product.getId(), quantity);

        Assertions.assertDoesNotThrow(() ->
                basketProductService.addProductToBasket(basket.getId(), nowMember.getId(), requestDTO)
        );

        assertThat(basket.getMember().getId()).isEqualTo(nowMember.getId());
    }

    @Test
    public void 찾는_상품이_없으면_예외가_발생한다() {
        // given
        int quantity = 10;
        long differentProductId = product.getId() + 1L;
        AddBasketProductRequestDTO requestDTO = AddBasketProductRequestDTO.of(differentProductId, quantity);

        // when // then
        BusinessException ex = Assertions.assertThrows(BusinessException.class, () -> {
            basketProductService.addProductToBasket(basket.getId(), nowMember.getId(), requestDTO);
        });

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
        assertThat(product.getId()).isNotEqualTo(differentProductId);
    }

    @Test
    public void 찾는_상품이_있으면_예외가_발생_안_한다() {
        // given
        int quantity = 1;
        AddBasketProductRequestDTO requestDTO = AddBasketProductRequestDTO.of(product.getId(), quantity);

        // when // then
        Assertions.assertDoesNotThrow(() -> {
            basketProductService.addProductToBasket(basket.getId(), nowMember.getId(), requestDTO);
        });
    }


    @Test
    public void 상품재고_체크_시_장바구니_담고_남은_재고가_0_미만이면_예외가_발생한다() {
        // given

        // 장바구니에 상품 재고 초과 요청
        int quantity = 10;
        AddBasketProductRequestDTO requestDTO = AddBasketProductRequestDTO.of(product.getId(), quantity);

        // when // then
        BusinessException ex = Assertions.assertThrows(BusinessException.class, () -> {
            basketProductService.addProductToBasket(basket.getId(), nowMember.getId(), requestDTO);
        });

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_OUT_OF_STOCK);
        Assertions.assertFalse(product.hasEnoughStock(quantity));
    }


    @Test
    public void 상품재고_체크_시_장바구니_담고_남은_재고가_0_이상이면_예외가_발생한다() {
        categoryFruit.addProduct(product);
        categoryRepository.save(categoryFruit);

        // 장바구니에 상품 재고 미만 요청
        int quantity = 1;
        AddBasketProductRequestDTO requestDTO = AddBasketProductRequestDTO.of(product.getId(), quantity);

        // when // then
        Assertions.assertDoesNotThrow(() ->
                basketProductService.addProductToBasket(basket.getId(), nowMember.getId(), requestDTO)
        );

        // then
        Assertions.assertTrue(product.hasEnoughStock(quantity));
    }

    @Test
    public void 장바구니에_상품_추가_성공() {
        // given

        // when
        int quantity = 1;
        AddBasketProductRequestDTO requestDTO = AddBasketProductRequestDTO.of(product.getId(), quantity);

        BasketProductResponseDTO responseDTO = basketProductService.addProductToBasket(basket.getId(), nowMember.getId(), requestDTO);

        // then
        assertThat(responseDTO.getQuantity()).isEqualTo(requestDTO.getQuantity());
    }

}
