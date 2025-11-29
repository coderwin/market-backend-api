package com.market.allra.web.controller;

import com.market.allra.service.ProductService;
import com.market.allra.web.dto.cond.ProductSearchCond;
import com.market.allra.web.dto.response.ApiResponseDTO;
import com.market.allra.web.dto.response.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/products")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<ProductResponseDTO>>> search(
            ProductSearchCond cond,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponseDTO.success(productService.search(cond, PageRequest.of(page, size))));
    }
}
