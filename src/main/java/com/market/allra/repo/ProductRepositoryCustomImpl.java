package com.market.allra.repo;

import com.market.allra.domain.Product;
import com.market.allra.domain.enums.StockStatus;
import com.market.allra.domain.enums.YesNo;
import com.market.allra.web.dto.cond.ProductSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.market.allra.domain.QCategory.category;
import static com.market.allra.domain.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Page<Product> search(ProductSearchCond cond, Pageable pageable) {
        // content 생성
        List<Product> contents = query.selectFrom(product)
                .join(product.category, category).fetchJoin()
                .where(
                        categoryIdEq(cond.getCategoryId())
                        , nameContains(cond.getKeyword())
                        , priceGoe(cond.getMinPrice())
                        , priceLoe(cond.getMaxPrice())
                        , product.deleteYN.eq(YesNo.N)
                )
                .orderBy(product.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy()
                .fetch();

        // totalCount 생성
        Long totalCount = query.select(product.count())
                .from(product)
                .join(product.category, category)
                .where(
                        categoryIdEq(cond.getCategoryId())
                        , nameContains(cond.getKeyword())
                        , priceGoe(cond.getMinPrice())
                        , priceLoe(cond.getMaxPrice())
                        , product.deleteYN.eq(YesNo.N)
                )
                .fetchOne();

        System.out.println(cond.getCategoryId());

        // return PageImpl
        return new PageImpl<>(contents, pageable, totalCount);
    }

    @Override
    public void updateDecreaseStock(Long productId, int quantity) {
        NumberExpression<Integer> newStock = product.stock.add(-quantity);

        query.update(product)
                // 재고 변경
                .set(product.stock, newStock)
                // 재고 상태 변경
                .set(
                    product.status,
                    new CaseBuilder()
                            .when(newStock.loe(0))
                            .then(StockStatus.SOLD_OUT)
                            .otherwise(product.status)
                )
                .where(product.id.eq(productId))
                .execute();
    }

    private static BooleanExpression priceLoe(Integer maxPrice) {
        return maxPrice != null ? product.price.loe(maxPrice) : null;
    }

    private static BooleanExpression priceGoe(Integer minPrice) {
        return minPrice != null ? product.price.goe(minPrice) : null;
    }

    private static BooleanExpression nameContains(String keyword) {
        return keyword != null ? product.name.contains(keyword) : null;
    }

    private static BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? product.category.id.eq(categoryId) : null;
    }
}
