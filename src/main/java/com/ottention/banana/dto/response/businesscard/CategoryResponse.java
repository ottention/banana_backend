package com.ottention.banana.dto.response.businesscard;

import com.ottention.banana.entity.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryResponse {
    private Long id;
    private String categoryName;

    //Entity -> Dto
    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
    }
}
