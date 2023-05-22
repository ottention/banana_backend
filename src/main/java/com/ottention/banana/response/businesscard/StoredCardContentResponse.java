package com.ottention.banana.response.businesscard;

import com.ottention.banana.entity.BusinessCardContent;
import com.ottention.banana.entity.StoredBusinessCard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoredCardContentResponse {

    private Long cid;  //content id
    private String content;
    private int xAxis;
    private int yAxis;
    private Long bid;  //businesscard id

    //Entity -> Dto
    public StoredCardContentResponse(BusinessCardContent businessCardContent) {
        this.cid = businessCardContent.getId();
        this.content = businessCardContent.getContent();
        this.xAxis = businessCardContent.getXAxis();
        this.yAxis = businessCardContent.getYAxis();
        this.bid = businessCardContent.getBusinessCard().getId();
    }
}
