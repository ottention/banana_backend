package com.ottention.banana.dto.response.businesscard;

import com.ottention.banana.dto.BusinessCardContentDto;
import com.ottention.banana.entity.BusinessCardContent;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class BusinessCardResponse {

    private final List<BusinessCardContentDto> contents;
    private final List<ImageResponse> images;

    public static BusinessCardResponse toBusinessCard(List<BusinessCardContent> businessCardContents, List<String> imageUrls) {
        return BusinessCardResponse.builder()
                .contents(businessCardContents.stream().map(c ->
                        BusinessCardContentDto.toContent(c))
                        .collect(toList()))
                .images(imageUrls.stream().map(i -> new ImageResponse(i))
                        .collect(toList()))
                .build();
    }

    @Builder
    public BusinessCardResponse(List<BusinessCardContentDto> contents, List<ImageResponse> images) {
        this.contents = contents;
        this.images = images;
    }

}
