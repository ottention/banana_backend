package com.ottention.banana.dto.request;

import com.ottention.banana.dto.BusinessCardContentDto;
import com.ottention.banana.dto.ImageDto;
import com.ottention.banana.dto.LinkDto;
import lombok.Getter;

import java.util.List;

@Getter
public class SaveBusinessCardRequest {
    private Boolean isPublic;
    private Boolean isPresent;

    private List<BusinessCardContentDto> frontContents;
    private List<LinkDto> frontLinks;
    private List<ImageDto> frontImages;
    private String frontTemplateColor;

    private List<BusinessCardContentDto> backContents;
    private List<LinkDto> backLinks;
    private List<ImageDto> backImages;
    private String backTemplateColor;

    private List<String> tags;

    public SaveBusinessCardRequest(Boolean isPublic, Boolean isPresent,
                                   List<BusinessCardContentDto> frontContents, List<LinkDto> frontLinks, List<ImageDto> frontImages, String frontTemplateColor,
                                   List<BusinessCardContentDto> backContents, List<LinkDto> backLinks, List<ImageDto> backImages, String backTemplateColor,
                                   List<String> tags) {
        this.isPublic = isPublic;
        this.isPresent = isPresent;
        this.frontContents = frontContents;
        this.frontLinks = frontLinks;
        this.frontImages = frontImages;
        this.frontTemplateColor = frontTemplateColor;
        this.backContents = backContents;
        this.backLinks = backLinks;
        this.backImages = backImages;
        this.backTemplateColor = backTemplateColor;
        this.tags = tags;
    }

}
