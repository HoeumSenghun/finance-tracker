package com.financetracker.service;

import com.financetracker.domain.entity.Category;
import com.financetracker.dto.request.CategoryRequest;
import com.financetracker.dto.response.CategoryResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CategoryService {

    List<CategoryResponse> findAllForCurrentUser();

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    Map<String, String> delete(Long id);

    Category findOwnedCategoryEntity(Long id, UUID userId);
}
