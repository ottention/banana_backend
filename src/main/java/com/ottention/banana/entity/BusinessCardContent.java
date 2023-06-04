package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class BusinessCardContent extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "content_id")
    private Long id;

    private String content;

    @Enumerated(STRING)
    private ContentSize contentSize;

    @Embedded
    private ContentCoordinate coordinate;
    private boolean isFront;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "business_card_id")
    private BusinessCard businessCard;

    public static BusinessCardContent updateBusinessCardContent(String content, ContentSize contentSize,
                                                                ContentCoordinate coordinate, boolean isFront) {
        return BusinessCardContent.builder()
                .content(content)
                .contentSize(contentSize)
                .coordinate(coordinate)
                .isFront(isFront)
                .build();
    }

    @Builder
    public BusinessCardContent(String content, ContentSize contentSize, ContentCoordinate coordinate, boolean isFront) {
        this.content = content;
        this.contentSize = contentSize;
        this.coordinate = coordinate;
        this.isFront = isFront;
    }

    public void addBusinessCard(BusinessCard businessCard) {
        this.businessCard = businessCard;
    }

}
