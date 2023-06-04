package com.ottention.banana.response.businesscard;

import com.ottention.banana.entity.BusinessCardContent;
import com.ottention.banana.entity.ContentCoordinate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusinessCardContentDto {

    private String content;
    private String contentSize;
    private ContentCoordinate coordinate;

    public static BusinessCardContentDto toContent(BusinessCardContent businessCardContent) {
        return BusinessCardContentDto.builder()
                .businessCardContent(businessCardContent)
                .build();
    }

    @Builder
    public BusinessCardContentDto(BusinessCardContent businessCardContent) {
        this.content = businessCardContent.getContent();
        this.contentSize = String.valueOf(businessCardContent.getContentSize());
        this.coordinate = businessCardContent.getCoordinate();
    }

}
