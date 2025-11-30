package com.market.allra.service;

import com.market.allra.domain.Basket;
import com.market.allra.domain.BasketProduct;
import com.market.allra.domain.Category;
import com.market.allra.domain.Member;
import com.market.allra.domain.Product;
import com.market.allra.domain.enums.StockStatus;
import com.market.allra.exception.BusinessException;
import com.market.allra.repo.BasketProductRepository;
import com.market.allra.repo.BasketRepository;
import com.market.allra.repo.CategoryRepository;
import com.market.allra.repo.MemberRepository;
import com.market.allra.web.dto.AddBasketProductRequestDTO;
import com.market.allra.web.dto.BasketProductResponseDTO;
import com.market.allra.web.dto.UpdateBasketProductRequestDTO;
import com.market.allra.web.dto.UpdateBasketProductResponseDTO;
import com.market.allra.web.dto.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
    @Autowired
    private BasketProductRepository basketProductRepository;

    private Member nowMember;
    private Basket basket;
    private Category categoryFruit;
    private Product product;
    private Product presentProduct;

    @Nested
    class AddProductToBasketTest {

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
            presentProduct = new Product("배", 1000, 10, StockStatus.IN_STOCK, categoryFruit);
            categoryFruit = new Category("과일");

            categoryFruit.addProduct(product);
            categoryFruit.addProduct(presentProduct);
            categoryRepository.save(categoryFruit);
        }

        /// ////////////////////////////////// 장바구니-상품 추가 테스트 ///////////////////////////////////////////////
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
            long differentProductId = product.getId() + 10L;
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
        public void 장바구니에_같은_상품이_존재할_때_추가하려는_수량과_기존_수량의_합이_상품의_재고_수량을_넘으면_예외가_발생한다() {
            // given
            // 회원 바구니에 상품 추가
            // 장바구니에 상품 등록
            int presentQuantity = 5;
            AddBasketProductRequestDTO previousRequestDTO = AddBasketProductRequestDTO.of(presentProduct.getId(), presentQuantity);

            BasketProduct basketProduct = BasketProduct.create(previousRequestDTO.getQuantity(), basket, presentProduct);
            basketProductRepository.save(basketProduct);

            // when
            // 같은 상품 또 추가
            int quantity = 10;
            AddBasketProductRequestDTO requestDTO = AddBasketProductRequestDTO.of(presentProduct.getId(), quantity);

            BusinessException ex = Assertions.assertThrows(BusinessException.class, () -> basketProductService.addProductToBasket(basket.getId(), nowMember.getId(), requestDTO));

            // then
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_OUT_OF_STOCK);
            assertThat(basketProduct.getQuantity() + quantity).isGreaterThan(presentProduct.getStock());
        }

        @Test
        public void 장바구니에_같은_상품이_존재하면_기존_장바구니_상품에_수량을_더한다() {
            // given
            // 회원 바구니에 상품 추가
            // 장바구니에 상품 등록
            int presentQuantity = 5;
            AddBasketProductRequestDTO previousRequestDTO = AddBasketProductRequestDTO.of(presentProduct.getId(), presentQuantity);

            BasketProduct basketProduct = BasketProduct.create(previousRequestDTO.getQuantity(), basket, presentProduct);
            basketProductRepository.save(basketProduct);

            // when
            // 같은 상품 또 추가
            int quantity = 2;
            AddBasketProductRequestDTO requestDTO = AddBasketProductRequestDTO.of(presentProduct.getId(), quantity);

            BasketProductResponseDTO responseDTO = basketProductService.addProductToBasket(basket.getId(), nowMember.getId(), requestDTO);

            // then
            assertThat(basket.getBasketProdcutList().size()).isEqualTo(1);
            assertThat(responseDTO.getQuantity()).isEqualTo(presentQuantity + quantity);
            assertThat(responseDTO.getBasket_product_id()).isEqualTo(basketProduct.getId());
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


    @Nested
    class UpdateProductToBasketTest {

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
            presentProduct = new Product("배", 1000, 10, StockStatus.IN_STOCK, categoryFruit);
            categoryFruit = new Category("과일");

            categoryFruit.addProduct(product);
            categoryFruit.addProduct(presentProduct);
            categoryRepository.save(categoryFruit);
        }

        /// ////////////////////////////////// 장바구니-상품 수정 테스트 ///////////////////////////////////////////////
        @Test
        public void 사용자의_장바구니_찾는_중_없으면_예외가_발생한다_from_udpate() {
            // given
            Long memberId = 999L;
            Long basketId = 999L;
            Long productId = 999L;
            int quantity = 1;

            UpdateBasketProductRequestDTO requestDTO = UpdateBasketProductRequestDTO.of(quantity);

            // when // then
            BusinessException ex = Assertions.assertThrows(BusinessException.class, () -> {
                basketProductService.updateProductToBasket(basketId, productId, memberId, requestDTO);
            });

            // then
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.BASKET_NOT_FOUND);
        }

        @Test
        public void 사용자의_장바구니_찾는_중_있으면_예외가_발생_안_한다_from_update() {
            // given
            int quantity = 1;
            saveBasketProduct(quantity);

            UpdateBasketProductRequestDTO requestDTO = UpdateBasketProductRequestDTO.of(quantity);

            // when // then
            Assertions.assertDoesNotThrow(() ->
                    basketProductService.updateProductToBasket(basket.getId(), product.getId(), nowMember.getId(), requestDTO)
            );

            assertThat(basket.getMember().getId()).isEqualTo(nowMember.getId());
        }

        private void saveBasketProduct(int quantity) {
            BasketProduct basketProduct = BasketProduct.create(quantity, basket, product);
            basketProductRepository.save(basketProduct);
        }

        @Test
        public void 요청_장바구니_상품이_존재하지_않으면_예외가_발생한다() {
            // given
            Long productId = 999L;
            int quantity = 1;

            UpdateBasketProductRequestDTO requestDTO = UpdateBasketProductRequestDTO.of(quantity);

            // when // then
            BusinessException ex = Assertions.assertThrows(BusinessException.class, () ->
                    basketProductService.updateProductToBasket(basket.getId(), productId, nowMember.getId(), requestDTO)
            );

            // then
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.BASKET_PRODUCT_NOT_FOUND);
        }

        @Test
        public void 요청_장바구니_상품이_존재하면_예외가_발생하지_않는다() {
            // given
            int quantity = 1;
            // basketProduct 등록
            saveBasketProduct(quantity);

            UpdateBasketProductRequestDTO requestDTO = UpdateBasketProductRequestDTO.of(quantity);

            // when // then
            Assertions.assertDoesNotThrow(() ->
                    basketProductService.updateProductToBasket(basket.getId(), product.getId(), nowMember.getId(), requestDTO)
            );
        }

        @Test
        public void 상품_재고_체크_시_수정_요청_수량과_재고_수량의_차이가_0_미만이면_예외가_발생한다() {
            // given
            int quantity = 10;
            // basketProduct 등록
            saveBasketProduct(quantity);

            UpdateBasketProductRequestDTO requestDTO = UpdateBasketProductRequestDTO.of(quantity);

            // when
            BusinessException ex = Assertions.assertThrows(BusinessException.class, () ->
                    basketProductService.updateProductToBasket(basket.getId(), product.getId(), nowMember.getId(), requestDTO)
            );

            // then
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_OUT_OF_STOCK);
            assertThat(requestDTO.getQuantity()).isGreaterThan(product.getStock());
        }


        @Test
        public void 상품_재고_체크_시_수정_요청_수량과_재고_수량의_차이가_0_이상이면_예외가_발생하지_않는다() {
            // given
            int quantity = 1;
            // basketProduct 등록
            saveBasketProduct(quantity);

            UpdateBasketProductRequestDTO requestDTO = UpdateBasketProductRequestDTO.of(quantity);

            // when
            Assertions.assertDoesNotThrow(() ->
                    basketProductService.updateProductToBasket(basket.getId(), product.getId(), nowMember.getId(), requestDTO)
            );

            // then
            assertThat(requestDTO.getQuantity()).isLessThanOrEqualTo(product.getStock());
        }

        @Test
        public void 장바구니_상품_수량_변경_성공() {
            // given
            int quantity = 1;
            // basketProduct 등록
            saveBasketProduct(quantity);

            UpdateBasketProductRequestDTO requestDTO = UpdateBasketProductRequestDTO.of(quantity);

            // when
            UpdateBasketProductResponseDTO responseDTO = basketProductService.updateProductToBasket(basket.getId(), product.getId(), nowMember.getId(), requestDTO);


            // then
            assertThat(responseDTO.getQuantity()).isEqualTo(requestDTO.getQuantity());
            assertThat(responseDTO.getProductId()).isEqualTo((product.getId()));
            assertThat(responseDTO.getBasketId()).isEqualTo((basket.getId()));
        }
    }

    /// ////////////////////////////////// 장바구니-상품 삭제 테스트 ///////////////////////////////////////////////

    @Nested
    class DeleteProductToBasket {

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
            presentProduct = new Product("배", 1000, 10, StockStatus.IN_STOCK, categoryFruit);
            categoryFruit = new Category("과일");

            categoryFruit.addProduct(product);
            categoryFruit.addProduct(presentProduct);
            categoryRepository.save(categoryFruit);
        }

        @Test
        public void 사용자의_장바구니_찾는_중_없으면_예외가_발생한다() {
            // given
            Long memberId = 999L;
            Long basketId = 999L;
            Long productId = 999L;

            // when // then
            BusinessException ex = Assertions.assertThrows(BusinessException.class, () -> {
                basketProductService.deleteProductToBasket(basketId, productId, memberId);
            });

            // then
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.BASKET_NOT_FOUND);
        }

        @Test
        public void 사용자의_장바구니_찾는_중_있으면_예외가_발생_안_한다() {
            // given
            int quantity = 1;
            saveBasketProduct(quantity);

            UpdateBasketProductRequestDTO requestDTO = UpdateBasketProductRequestDTO.of(quantity);

            // when // then
            Assertions.assertDoesNotThrow(() ->
                    basketProductService.deleteProductToBasket(basket.getId(), product.getId(), nowMember.getId())
            );

            assertThat(basket.getMember().getId()).isEqualTo(nowMember.getId());
        }

        private BasketProduct saveBasketProduct(int quantity) {
            BasketProduct basketProduct = BasketProduct.create(quantity, basket, product);
            basketProductRepository.save(basketProduct);

            return basketProduct;
        }

        @Test
        public void 찾는_장바구니_상품이_없어도_삭제는_성공한다() {
            // given
            int quantity = 1;
            saveBasketProduct(quantity);

            // when // then
            Long notPresentProductId = product.getId() + 10L;

            basketProductService.deleteProductToBasket(basket.getId(), notPresentProductId, nowMember.getId());

            // then
            assertThat(basketProductRepository.findByBasketIdAndProductId(basket.getId(), notPresentProductId)).isEmpty();
        }

        @Test
        public void 장바구니_내에_존재하는_상품이_삭제된다() {
            // given
            int quantity = 1;
            BasketProduct basketProduct = saveBasketProduct(quantity);

            // when
            basketProductService.deleteProductToBasket(basket.getId(), product.getId(), nowMember.getId());

            // then
            assertThat(basket.getBasketProdcutList().contains(basketProduct)).isFalse();
            assertThat(basketProductRepository.findById(basketProduct.getId())).isEmpty();
        }
    }
}
