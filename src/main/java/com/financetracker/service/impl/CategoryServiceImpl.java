package com.financetracker.service.impl;

import com.financetracker.domain.entity.Category;
import com.financetracker.domain.entity.User;
import com.financetracker.dto.request.CategoryRequest;
import com.financetracker.dto.response.CategoryResponse;
import com.financetracker.exception.ForbiddenException;
import com.financetracker.mapper.CategoryMapper;
import com.financetracker.repository.CategoryRepository;
import com.financetracker.service.CategoryService;
import com.financetracker.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAllForCurrentUser() {
        User user = userService.getCurrentUser();
        return categoryRepository.findByUserId(user.getId()).stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        User user = userService.getCurrentUser();
        Category category = categoryMapper.toEntity(request);
        category.setUser(user);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getOwnedCategory(id);
        categoryMapper.updateEntity(request, category);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public Map<String, String> delete(Long id) {
        categoryRepository.delete(getOwnedCategory(id));
        return Map.of("message", "Category deleted");
    }

    @Override
    public Category findOwnedCategoryEntity(Long id, UUID userId) {
        return categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + id));
    }

    private Category getOwnedCategory(Long id) {
        User user = userService.getCurrentUser();
        return categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ForbiddenException("Category does not belong to you"));
    }
}
