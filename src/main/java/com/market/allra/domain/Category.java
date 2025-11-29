package com.market.allra.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST})
    private List<Product> productList = new ArrayList<>();

    /* 비즈니스 로직 */

    // 연관관계 매핑 메서드
    public void addProduct(Product product) {
        product.setCategory(this);
        productList.add(product);
    }


}
