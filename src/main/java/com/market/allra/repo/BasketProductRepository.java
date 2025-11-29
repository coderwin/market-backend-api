package com.market.allra.repo;

import com.market.allra.domain.BasketProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketProductRepository extends JpaRepository<BasketProduct, Long> {
}
