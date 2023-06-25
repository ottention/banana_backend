package com.ottention.banana.dto.response.businesscard;

import com.ottention.banana.entity.StoredBusinessCard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoredCardResponse {
    private Long sid;
    private String name;
    private Boolean isBookmarked;
    private Long cid;  //businesscard id

    private List<StoredCardContentResponse> cardContentResponses;

    //Entity -> Dto
    public StoredCardResponse(StoredBusinessCard storedBusinessCard, List<StoredCardContentResponse> cardContentResponses) {
        this.sid = storedBusinessCard.getId();
        this.name = storedBusinessCard.getName();
        this.isBookmarked = storedBusinessCard.getIsBookmarked();
        this.cid = storedBusinessCard.getBusinessCard().getId();
        this.cardContentResponses = cardContentResponses;
    }
}