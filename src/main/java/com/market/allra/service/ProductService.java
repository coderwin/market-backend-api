package com.market.allra.service;

import com.market.allra.web.dto.cond.ProductSearchCond;
import com.market.allra.web.dto.response.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductService {
    Page<ProductResponseDTO> search(ProductSearchCond cond, PageRequest pageRequest);
}
