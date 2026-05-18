// src/main/java/com/financetracker/category/CategoryMapper.java
package com.financetracker.category;

import com.financetracker.category.dto.CategoryRequest;
import com.financetracker.category.dto.CategoryResponse;
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
