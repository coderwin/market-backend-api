package com.market.allra.repo;

import com.market.allra.domain.Product;
import com.market.allra.web.dto.cond.ProductSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> search(ProductSearchCond cond, Pageable pageable);

    void updateDecreaseStock(Long productId, int quantity);
}
