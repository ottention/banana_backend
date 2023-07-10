package com.ottention.banana.dto.response.businesscard;

import com.ottention.banana.dto.BusinessCardContentDto;
import com.ottention.banana.dto.ImageDto;
import com.ottention.banana.dto.LinkDto;
import com.ottention.banana.entity.BusinessCardContent;
import com.ottention.banana.entity.Image;
import com.ottention.banana.entity.Link;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BackBusinessCardResponse {
    private List<BusinessCardContentDto> backContents;
    private List<LinkDto> backLinks;
    private List<ImageDto> backImages;
    private String backTemplateColor;

    @Builder
    public BackBusinessCardResponse(List<BusinessCardContent> backContents, List<Link> backLinks, List<Image> backImages, String backTemplateColor) {
        this.backContents = backContents.stream().map(BusinessCardContentDto::new).toList();
        this.backLinks = backLinks.stream().map(LinkDto::new).toList();
        this.backImages = backImages.stream().map(ImageDto::new).toList();
        this.backTemplateColor = backTemplateColor;
    }
}
