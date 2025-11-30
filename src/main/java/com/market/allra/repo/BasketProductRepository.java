package com.market.allra.repo;

import com.market.allra.domain.BasketProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasketProductRepository extends JpaRepository<BasketProduct, Long>, BasketProductRepositoryCustom {
    Optional<BasketProduct> findByBasketIdAndProductId(Long basketId, Long productId);
}
