package com.market.allra.web.dto.response;

import com.market.allra.domain.Category;
import lombok.Builder;
import lombok.Getter;

/**
 * 카테고리 응답 DTO
 */
@Getter
public class CategoryResponseDTO {
    private Long id;
    private String name;

    @Builder
    public CategoryResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /* 비즈니스 로직 */

    public static CategoryResponseDTO create(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
