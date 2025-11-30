package com.market.allra.service;

import com.market.allra.domain.Order;
import com.market.allra.domain.enums.PaymentMethod;
import com.market.allra.exception.BusinessException;
import com.market.allra.web.dto.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles(value = "test")
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    public void 주문이_null_이면_예외_발생() {
        // given
        PaymentMethod paymentMethod = PaymentMethod.CARD;
        Order order = null;

        // when
        BusinessException ex = Assertions.assertThrows(BusinessException.class, ()->{
            paymentService.createPayment(order, paymentMethod);
        });

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND);





    }






}
