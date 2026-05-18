package com.financetracker.mapper;

import com.financetracker.domain.entity.Category;
import com.financetracker.dto.request.CategoryRequest;
import com.financetracker.dto.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .build();
    }

    public Category toEntity(CategoryRequest request) {
        if (request == null) {
            return null;
        }
        return Category.builder()
                .name(request.getName())
                .type(request.getType())
                .build();
    }

    public void updateEntity(CategoryRequest request, Category category) {
        if (request == null || category == null) {
            return;
        }
        category.setName(request.getName());
        category.setType(request.getType());
    }
}
