package com.ottention.banana.dto;

import com.ottention.banana.entity.Link;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class LinkDto {

    private String link;
    private String linkText;
    private Boolean isFront;

    public LinkDto(Link link) {
        this.link = link.getLink();
        this.linkText = link.getLinkText();
        this.isFront = link.getIsFront();
    }

}
