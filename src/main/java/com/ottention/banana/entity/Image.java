package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Image extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String imageUrl; //이미지 주소
    private Boolean isFront; //앞, 뒤 구분

    @Embedded
    private Coordinate coordinate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "business_card_id")
    private BusinessCard businessCard;

    public static Image createImage(String imageUrl, Boolean isFront, BusinessCard businessCard, Coordinate coordinate) {
        return Image.builder()
                .imageUrl(imageUrl)
                .isFront(isFront)
                .businessCard(businessCard)
                .coordinate(coordinate)
                .build();
    }

    @Builder
    public Image(String imageUrl, Boolean isFront, Coordinate coordinate, BusinessCard businessCard) {
        this.imageUrl = imageUrl;
        this.isFront = isFront;
        this.coordinate = coordinate;
        this.businessCard = businessCard;
    }
}
