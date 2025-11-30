package com.market.allra.repo;

import com.market.allra.domain.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findByIdAndMemberId(Long basketId, Long memberId);
}
