package com.market.allra.service;

import com.market.allra.domain.Order;
import com.market.allra.domain.Payment;
import com.market.allra.domain.enums.PaymentMethod;

public interface PaymentService {
    Payment createPayment(Order order, PaymentMethod paymentMethod);
}
