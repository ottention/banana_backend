package com.ottention.banana.response;

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
    private final List<TagResponse> tags;

    public static BusinessCardResponse toBusinessCard(List<BusinessCardContent> businessCardContents, List<String> imageUrls,
                                                      List<BusinessCardTag> tags) {
        return BusinessCardResponse.builder()
                .contents(businessCardContents.stream().map(c ->
                        ContentResponse.toContent(c.getContent(), c.getXAxis(), c.getYAxis()))
                        .collect(toList()))
                .images(imageUrls.stream().map(i -> new ImageResponse(i))
                        .collect(toList()))
                .tags(tags.stream().map(t -> new TagResponse(t.getTag().getName()))
                        .collect(toList()))
                .build();
    }

    @Builder
    public BusinessCardResponse(List<ContentResponse> contents, List<ImageResponse> images, List<TagResponse> tags) {
        this.contents = contents;
        this.images = images;
        this.tags = tags;
    }
}
