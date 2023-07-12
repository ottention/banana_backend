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
public class FrontBusinessCardResponse {
    private List<BusinessCardContentDto> frontContents;
    private List<LinkDto> frontLinks;
    private List<ImageDto> frontImages;
    private String frontTemplateColor;

    @Builder
    public FrontBusinessCardResponse(List<BusinessCardContent> frontContents, List<Link> frontLinks, List<Image> frontImages, String frontTemplateColor) {
        this.frontContents = frontContents.stream().map(BusinessCardContentDto::new).toList();
        this.frontLinks = frontLinks.stream().map(LinkDto::new).toList();
        this.frontImages = frontImages.stream().map(ImageDto::new).toList();
        this.frontTemplateColor = frontTemplateColor;
    }
}
