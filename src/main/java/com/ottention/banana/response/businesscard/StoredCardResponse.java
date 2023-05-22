package com.ottention.banana.response.businesscard;

import com.ottention.banana.entity.StoredBusinessCard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoredCardResponse {
    private String name;
    private Boolean isBookmarked;
    private Long cid;  //businesscard id

    private List<StoredCardContentResponse> storedCardContents;

    //Entity -> Dto
    public StoredCardResponse(StoredBusinessCard storedBusinessCard) {
        this.name = storedBusinessCard.getName();
        this.isBookmarked = storedBusinessCard.getIsBookmarked();
        this.cid = storedBusinessCard.getBusinessCard().getId();
    }
}
