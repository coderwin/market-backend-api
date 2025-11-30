package com.market.allra.service;

import com.market.allra.domain.Basket;
import com.market.allra.domain.BasketProduct;
import com.market.allra.domain.Category;
import com.market.allra.domain.Member;
import com.market.allra.domain.Order;
import com.market.allra.domain.Product;
import com.market.allra.domain.enums.StockStatus;
import com.market.allra.domain.enums.YesNo;
import com.market.allra.exception.BusinessException;
import com.market.allra.repo.BasketProductRepository;
import com.market.allra.repo.BasketRepository;
import com.market.allra.repo.CategoryRepository;
import com.market.allra.repo.MemberRepository;
import com.market.allra.repo.ProductRepository;
import com.market.allra.web.dto.exception.ErrorCode;
import jakarta.persistence.EntityManager;
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
public class OrderServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    BasketProductService basketProductService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BasketProductRepository basketProductRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    EntityManager em;

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

        // 상품 추가
        product = new Product("사과", 1000, 1, StockStatus.IN_STOCK, categoryFruit);
        Product product2 = new Product("배", 1000, 10, StockStatus.IN_STOCK, categoryFruit);
        Product product3 = new Product("포도", 4000, 10, StockStatus.IN_STOCK, categoryFruit);
        Product product4 = new Product("망고", 1000, 2, StockStatus.IN_STOCK, categoryFruit);
        Product product5 = new Product("토마토", 3000, 10, StockStatus.IN_STOCK, categoryFruit);
        Product product6 = new Product("감", 10000, 3, StockStatus.IN_STOCK, categoryFruit);
        categoryFruit = new Category("과일");

        categoryFruit.addProduct(product);
        categoryFruit.addProduct(product2);
        categoryFruit.addProduct(product3);
        categoryFruit.addProduct(product4);
        categoryFruit.addProduct(product5);
        categoryFruit.addProduct(product6);
        categoryRepository.save(categoryFruit);

        // 장바구니-상품 추가
        getBasketProduct(1, basket, product);
        getBasketProduct(1, basket, product2);
        getBasketProduct(1, basket, product3);
        getBasketProduct(1, basket, product4);
        getBasketProduct(1, basket, product5);
        getBasketProduct(1, basket, product6);

        basketRepository.save(basket);
    }

    private BasketProduct getBasketProduct(int quantity, Basket basket, Product product) {
        return BasketProduct.create(quantity, basket, product);
    }

    @Test
    public void 사용자_장바구니_찾는_중_없으면_에외가_발생한다() {
        // given
        Long memberId = 999L;
        Long basketId = 999L;

        // when // then
        BusinessException ex = Assertions.assertThrows(BusinessException.class, () -> {
                orderService.createOrder(basketId, memberId);
        });

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.BASKET_NOT_FOUND);
    }

    @Test
    public void 사용자의_장바구니_찾는_중_있으면_예외가_발생_안_한다() {
        // given

        // when // then
        Assertions.assertDoesNotThrow(() ->
                orderService.createOrder(basket.getId(), nowMember.getId())
        );

        assertThat(basket.getMember().getId()).isEqualTo(nowMember.getId());
    }


    @Test
    public void 재고_확인_시_재고가_부족하면_예외가_발생한다() {
        // given
        Product product7 = new Product("감자", 1000, 10, StockStatus.IN_STOCK, categoryFruit);

        categoryFruit.addProduct(product7);
        productRepository.save(product7);
        basketProductRepository.save(getBasketProduct(11, basket, product7));

        // when // then
        BusinessException ex = Assertions.assertThrows(BusinessException.class, () -> {
            orderService.createOrder(basket.getId(), nowMember.getId());
        });

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_OUT_OF_STOCK);
    }

    // 동시성 처리로 flush 후에 다시 개수 비교한다.
    @Test
    public void 재고_확인_하며_재고를_수량만큼_차감한다() {
        // given
        int originStock = 10;
        Product product7 = new Product("감자", 1000, originStock, StockStatus.IN_STOCK, categoryFruit);
        productRepository.save(product7);

        BasketProduct basketProduct = getBasketProduct(2, basket, product7);
        basketProductRepository.save(basketProduct);

        // when // then
        orderService.createOrder(basket.getId(), nowMember.getId());

        em.flush();
        em.clear();

        Product updateProduct = productRepository.findById(product7.getId()).get();

        // then
        assertThat(updateProduct.getStock()).isEqualTo(originStock - basketProduct.getQuantity());
    }

    @Test
    public void 재고_확인_하며_재고를_수량차감_후_재고가_0_이되면_재고상태를_변경시킨다() {
        // given
        int originStock = 10;
        Product product7 = new Product("감자", 1000, originStock, StockStatus.IN_STOCK, categoryFruit);

        categoryFruit.addProduct(product7);
        productRepository.save(product7);
        BasketProduct basketProduct = getBasketProduct(10, basket, product7);
        basketProductRepository.save(basketProduct);

        // when // then
        orderService.createOrder(basket.getId(), nowMember.getId());

        em.flush();
        em.clear();

        Product updateProduct = productRepository.findById(product7.getId()).get();

        // then
        assertThat(updateProduct.getStock()).isEqualTo(originStock - basketProduct.getQuantity());
        assertThat(updateProduct.getStatus()).isEqualTo(StockStatus.SOLD_OUT);
    }

    @Test
    public void 주문_상품_생성에_성공한다() {
        // given
        int originStock = 10;
        Product product7 = new Product("감자", 1000, originStock, StockStatus.IN_STOCK, categoryFruit);

        categoryFruit.addProduct(product7);
        productRepository.save(product7);
        BasketProduct basketProduct = getBasketProduct(10, basket, product7);
        basketProductRepository.save(basketProduct);

        // when // then
        Order order = orderService.createOrder(basket.getId(), nowMember.getId());

        // then
        assertThat(order.getOrderProductList().size()).isEqualTo(basket.getBasketProdcutList().size());
    }


    // 마지막에
    @Test
    public void 삭제된_상품_품절된_상품은_orderItem_생성에서_뺀다_않는다() {
        // given
        Product product7 = new Product("감자", 1000, 10, StockStatus.IN_STOCK, categoryFruit, YesNo.Y);
        Product product8 = new Product("고구마", 1000, 0, StockStatus.SOLD_OUT, categoryFruit);

        categoryFruit.addProduct(product7);
        categoryFruit.addProduct(product8);
        productRepository.save(product7);
        productRepository.save(product8);
        basketProductRepository.save(getBasketProduct(2, basket, product7));
        basketProductRepository.save(getBasketProduct(2, basket, product8));

        // when // then
        Order order = orderService.createOrder(basket.getId(), nowMember.getId());

        // then
        assertThat(order.getOrderProductList().size()).isEqualTo(basket.getBasketProdcutList().size() - 2);
    }
}
