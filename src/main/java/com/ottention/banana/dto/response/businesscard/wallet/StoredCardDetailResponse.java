package com.ottention.banana.dto.response.businesscard.wallet;

import com.ottention.banana.dto.response.businesscard.BackBusinessCardResponse;
import com.ottention.banana.dto.response.businesscard.FrontBusinessCardResponse;
import com.ottention.banana.entity.Tag;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class StoredCardDetailResponse {
    private Long id;
    private String name;
    private FrontBusinessCardResponse front;
    private BackBusinessCardResponse back;
    private List<String> tags;

    @Builder
    public StoredCardDetailResponse(Long id, String name, FrontBusinessCardResponse front, BackBusinessCardResponse back, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.front = front;
        this.back = back;
        this.tags = tags.stream().map(Tag::getName).toList();
    }
}
