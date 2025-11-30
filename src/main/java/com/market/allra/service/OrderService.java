package com.market.allra.service;

import com.market.allra.domain.Order;

public interface OrderService {
    Order createOrder(Long basketId, Long memberId);
}
