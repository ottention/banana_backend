package com.ottention.banana.service;

import com.ottention.banana.dto.response.businesscard.CategoryResponse;
import com.ottention.banana.entity.Category;
import com.ottention.banana.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    //카테고리 조회
    public List<CategoryResponse> getCategories(Long id) {
        List<Category> categories = categoryRepository.findAllByUserIdOrderByModifiedDateDesc(id);
        List<CategoryResponse> categoryResponses = new ArrayList<>();

        categories.forEach(c -> categoryResponses.add(new CategoryResponse(c)));
        return categoryResponses;
    }
}
