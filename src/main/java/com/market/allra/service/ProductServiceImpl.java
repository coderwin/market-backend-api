package com.market.allra.service;

import com.market.allra.domain.Product;
import com.market.allra.web.dto.cond.ProductSearchCond;
import com.market.allra.web.dto.response.ProductResponseDTO;
import com.market.allra.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponseDTO> search(ProductSearchCond cond, PageRequest pageRequest) {
        Page<Product> findProductList = productRepository.search(cond, pageRequest);

        return findProductList.map(ProductResponseDTO::create);
    }
}
