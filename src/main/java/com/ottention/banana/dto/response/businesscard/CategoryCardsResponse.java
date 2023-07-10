package com.ottention.banana.dto.response.businesscard;

import com.ottention.banana.dto.response.businesscard.wallet.StoredCardDetailResponse;
import com.ottention.banana.dto.response.businesscard.wallet.StoredCardPreviewResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CategoryCardsResponse {
    private String categoryName;
    private List<StoredCardPreviewResponse> storedCards;
}
