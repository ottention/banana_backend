package com.ottention.banana.service;

import com.ottention.banana.dto.response.businesscard.CategoryCardsResponse;
import com.ottention.banana.dto.response.businesscard.CategoryResponse;
import com.ottention.banana.entity.wallet.Category;
import com.ottention.banana.mapper.CategoryCardsMapper;
import com.ottention.banana.mapper.CategoryMapper;
import com.ottention.banana.repository.wallet.CategoryRepository;
import com.ottention.banana.service.wallet.StoredBusinessCardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryCardsMapper categoryCardsMapper;
    private final StoredBusinessCardService storedCardService;

    //카테고리 조회
    public List<CategoryResponse> getCategories(Long id) {
        List<Category> categories = categoryRepository.findAllByUserIdOrderByModifiedDateDesc(id);
        List<CategoryResponse> categoryResponses = new ArrayList<>();

        categories.forEach(c -> categoryResponses.add(categoryMapper.toResponse(c)));
        return categoryResponses;
    }

    private String getCategoryName(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        return category.getCategoryName();
    }

    public CategoryCardsResponse getCategoryCards(Long categoryId, Pageable pageable) {
        return categoryCardsMapper.toResponse(getCategoryName(categoryId), storedCardService.getStoredCardByCategory(categoryId, pageable));
    }
}
