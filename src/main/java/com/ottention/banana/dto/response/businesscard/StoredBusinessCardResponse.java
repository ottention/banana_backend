package com.ottention.banana.dto.response.businesscard;

import com.ottention.banana.dto.BusinessCardContentDto;
import com.ottention.banana.entity.BusinessCardContent;
import com.ottention.banana.entity.Image;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class StoredBusinessCardResponse {
    private Long id;
    private String name;
    private List<BusinessCardContentDto> contents;
    private List<ImageResponse> images;
    private List<TagResponse> tags;

    @Builder
    public StoredBusinessCardResponse(Long id, String name, List<BusinessCardContent> contents, List<Image> images, List<TagResponse> tags) {
        this.id = id;
        this.name = name;
        this.contents = contents.stream().map(BusinessCardContentDto::toContent).toList();
        this.images = images.stream().map(i -> new ImageResponse(i.getImageUrl(), i.getCoordinate())).toList();
        this.tags = tags;

    }
}
