package com.ottention.banana.dto.response.businesscard;

import com.ottention.banana.dto.BusinessCardContentDto;
import com.ottention.banana.dto.ImageDto;
import com.ottention.banana.dto.LinkDto;
import com.ottention.banana.entity.*;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BusinessCardResponse {
    private Long businessCardId;
    private Boolean isPublic;
    private Boolean isRepresent;
    private int likeCount;

    private List<BusinessCardContentDto> frontContents;
    private List<LinkDto> frontLinks;
    private List<ImageDto> frontImages;
    private String frontTemplateColor;

    private List<BusinessCardContentDto> backContents;
    private List<LinkDto> backLinks;
    private List<ImageDto> backImages;
    private String backTemplateColor;

    private List<String> tags;

    public BusinessCardResponse(BusinessCard businessCard, List<BusinessCardContent> frontContents,
                                List<Link> frontLinks, List<Image> frontImages,
                                List<BusinessCardContent> backContents,
                                List<Link> backLinks, List<Image> backImages,
                                List<Tag> tags
                                ) {
        this.businessCardId = businessCard.getId();
        this.isPublic = businessCard.getIsPublic();
        this.isRepresent = businessCard.getIsRepresent();
        this.likeCount = businessCard.getLikeCount();
        this.frontContents = frontContents.stream().map(c -> new BusinessCardContentDto(c))
                .collect(Collectors.toList());
        this.frontLinks = frontLinks.stream().map(l -> new LinkDto(l))
                .collect(Collectors.toList());
        this.frontImages = frontImages.stream().map(i -> new ImageDto(i))
                .collect(Collectors.toList());
        this.frontTemplateColor = businessCard.getFrontTemplateColor();
        this.backContents = backContents.stream().map(c -> new BusinessCardContentDto(c))
                .collect(Collectors.toList());
        this.backLinks = backLinks.stream().map(l -> new LinkDto(l))
                .collect(Collectors.toList());
        this.backImages = backImages.stream().map(i -> new ImageDto(i))
                .collect(Collectors.toList());
        this.backTemplateColor = businessCard.getBackTemplateColor();
        this.tags = tags.stream().map(t -> t.getName())
                .collect(Collectors.toList());
    }

}
