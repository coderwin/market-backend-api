package com.market.allra.web.dto.cond;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchCond {
    private Long categoryId;
    private String keyword;
    private Integer minPrice;
    private Integer maxPrice;

    @Builder
    public ProductSearchCond(Long categoryId, String keyword, Integer minPrice, Integer maxPrice) {
        this.categoryId = categoryId;
        this.keyword = keyword;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
}
