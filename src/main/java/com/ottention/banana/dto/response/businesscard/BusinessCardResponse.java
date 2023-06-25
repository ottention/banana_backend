package com.ottention.banana.dto.response.businesscard;

import com.ottention.banana.dto.BusinessCardContentDto;
import com.ottention.banana.entity.BusinessCardContent;
import com.ottention.banana.entity.Image;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class BusinessCardResponse {

    private final List<BusinessCardContentDto> contents;
    private final List<ImageResponse> images;
    private final List<TagResponse> tags;

    public static BusinessCardResponse toBusinessCard(List<BusinessCardContent> businessCardContents, List<Image> images,
                                                      List<TagResponse> tags) {
        return BusinessCardResponse.builder()
                .contents(businessCardContents.stream().map(c ->
                        BusinessCardContentDto.toContent(c))
                        .collect(toList()))
                .images(images.stream().map(i -> new ImageResponse(i.getImageUrl(), i.getCoordinate()))
                        .collect(toList()))
                .tags(tags)
                .build();
    }

    @Builder
    public BusinessCardResponse(List<BusinessCardContentDto> contents, List<ImageResponse> images, List<TagResponse> tags) {
        this.contents = contents;
        this.images = images;
        this.tags = tags;
    }

}
