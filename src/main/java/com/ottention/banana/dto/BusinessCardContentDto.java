package com.ottention.banana.dto;

import com.ottention.banana.entity.BusinessCardContent;
import com.ottention.banana.entity.Coordinate;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BusinessCardContentDto {

    private String content;
    private String contentSize;
    private Coordinate coordinate;
    private Boolean isFront;

    public BusinessCardContentDto(BusinessCardContent businessCardContent) {
        this.content = businessCardContent.getContent();
        this.contentSize = String.valueOf(businessCardContent.getContentSize());
        this.coordinate = businessCardContent.getCoordinate();
        this.isFront = businessCardContent.isFront();
    }

}
