package com.market.allra.service;


import com.market.allra.domain.Category;
import com.market.allra.domain.Product;
import com.market.allra.domain.enums.StockStatus;
import com.market.allra.web.dto.cond.ProductSearchCond;
import com.market.allra.web.dto.response.ProductResponseDTO;
import com.market.allra.repo.CategoryRepository;
import com.market.allra.repo.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles(value = "test")
public class ProductServiceTest {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    private Category categoryFruit;
    private Category categoryClothes;

    @BeforeEach
    public void init() {
        categoryFruit = new Category("과일");
        categoryClothes = new Category("옷");

        Product product1 = new Product("사과", 1000, 1, StockStatus.IN_STOCK, categoryFruit);
        Product product2 = new Product("바나나", 2000, 10, StockStatus.IN_STOCK, categoryFruit);
        Product product3 = new Product("포도", 500, 0, StockStatus.SOLD_OUT, categoryFruit);

        Product product4 = new Product("청바지", 10000, 10, StockStatus.IN_STOCK, categoryClothes);
        Product product5 = new Product("스웨터", 100000, 0, StockStatus.SOLD_OUT, categoryClothes);

        categoryFruit.addProduct(product1);
        categoryFruit.addProduct(product2);
        categoryFruit.addProduct(product3);

        categoryClothes.addProduct(product4);
        categoryClothes.addProduct(product5);

        categoryRepository.save(categoryFruit);
        categoryRepository.save(categoryClothes);
    }

    @Test
    void 상품을_카테고리별로_조회할_수_있다() {
        // given
        ProductSearchCond cond = ProductSearchCond.builder().categoryId(categoryFruit.getId()).build();

        PageRequest pageRequest = PageRequest.of(0, 2);

        // when
        Page<ProductResponseDTO> result = productService.search(cond, pageRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent().size()).isEqualTo(2);
    }

    @Test
    void 상품을_상품명으로_조회할_수_있다() {
        // given
        String keyword = "바";
        ProductSearchCond cond = ProductSearchCond.builder().keyword(keyword).build();

        PageRequest pageRequest = PageRequest.of(0, 3);

        // when
        Page<ProductResponseDTO> result = productService.search(cond, pageRequest);

        // then
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 상품을_가격범위로_조회할_수_있다() {
        // given
        int minPrice = 1000;
        int maxPrice = 10000;
        ProductSearchCond cond = ProductSearchCond.builder().minPrice(minPrice).maxPrice(maxPrice).build();

        PageRequest pageRequest = PageRequest.of(0, 2);

        // when
        Page<ProductResponseDTO> result = productService.search(cond, pageRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent().size()).isEqualTo(2);
    }


}
