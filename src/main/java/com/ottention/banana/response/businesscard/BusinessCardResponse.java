package com.ottention.banana.response.businesscard;

import com.ottention.banana.entity.BusinessCardContent;
import com.ottention.banana.entity.BusinessCardTag;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class BusinessCardResponse {

    private final List<ContentResponse> contents;
    private final List<ImageResponse> images;

    public static BusinessCardResponse toBusinessCard(List<BusinessCardContent> businessCardContents, List<String> imageUrls) {
        return BusinessCardResponse.builder()
                .contents(businessCardContents.stream().map(c ->
                        ContentResponse.toContent(c.getContent(), c.getWidth(), c.getWidth(), c.getXAxis(), c.getYAxis()))
                        .collect(toList()))
                .images(imageUrls.stream().map(i -> new ImageResponse(i))
                        .collect(toList()))
                .build();
    }

    @Builder
    public BusinessCardResponse(List<ContentResponse> contents, List<ImageResponse> images) {
        this.contents = contents;
        this.images = images;
    }
}
