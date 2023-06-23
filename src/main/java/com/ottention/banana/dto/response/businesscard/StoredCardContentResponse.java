package com.ottention.banana.dto.response.businesscard;

import com.ottention.banana.entity.BusinessCardContent;
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
        this.xAxis = businessCardContent.getCoordinate().getxAxis();
        this.yAxis = businessCardContent.getCoordinate().getyAxis();
        this.bid = businessCardContent.getBusinessCard().getId();
    }
}
