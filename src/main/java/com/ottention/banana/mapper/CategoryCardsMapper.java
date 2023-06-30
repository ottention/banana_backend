package com.ottention.banana.mapper;

import com.ottention.banana.dto.response.businesscard.CategoryCardsResponse;
import com.ottention.banana.dto.response.businesscard.StoredBusinessCardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryCardsMapper {

    CategoryCardsResponse toResponse(String categoryName, List<StoredBusinessCardResponse> storedCards);
}
