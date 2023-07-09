package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Link {

    @Id @GeneratedValue
    @Column(name = "link_id")
    private Long id;

    private String link;
    private String linkText;
    private Boolean isFront;

    @Embedded
    private Coordinate coordinate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "business_card_id")
    private BusinessCard businessCard;

    @Builder
    public Link(String link, String linkText, Boolean isFront, Coordinate coordinate, BusinessCard businessCard) {
        this.link = link;
        this.linkText = linkText;
        this.isFront = isFront;
        this.coordinate = coordinate;
        this.businessCard = businessCard;
    }

}
