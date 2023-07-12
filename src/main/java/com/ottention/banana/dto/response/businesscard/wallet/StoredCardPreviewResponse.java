package com.ottention.banana.dto.response.businesscard.wallet;

import com.ottention.banana.dto.response.businesscard.FrontBusinessCardResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoredCardPreviewResponse {
    private Long id;
    private String name;
    private FrontBusinessCardResponse front;

    @Builder
    public StoredCardPreviewResponse(Long id, String name, FrontBusinessCardResponse front) {
        this.id = id;
        this.name = name;
        this.front = front;
    }
}
