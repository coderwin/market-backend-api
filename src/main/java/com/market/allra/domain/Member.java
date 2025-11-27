package com.market.allra.domain;

import com.market.allra.domain.enums.YesNo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
public class Member {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(length = 100)
    private String address;
    @Enumerated(EnumType.STRING)
    private YesNo deleteYN = YesNo.N;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST})
    private List<Order> orderList = new ArrayList<>();

    /* 비즈니스 로직 */

    // 연관관계 편의 메서드
    public void addOrder(Order order) {
        order.setMember(this);
        orderList.add(order);
    }
}
