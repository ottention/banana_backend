package com.ottention.banana.mapper;

import com.ottention.banana.dto.response.businesscard.CategoryResponse;
import com.ottention.banana.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
}
