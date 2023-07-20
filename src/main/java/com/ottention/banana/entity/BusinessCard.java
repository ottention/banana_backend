package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class BusinessCard extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "business_card_id")
    private Long id;

    private Boolean isPublic; //공개 비공개
    private Boolean isRepresent; //대표 명함
    private String frontTemplateColor;
    private String backTemplateColor;
    private int likeCount; //좋아요 수

    @OneToOne
    @JoinColumn(name = "qrcode_id")
    private QRCode qrCode;

    @OneToMany(mappedBy = "businessCard", cascade = ALL)
    private List<BusinessCardContent> businessCardContents = new ArrayList<>();

    @OneToMany(mappedBy = "businessCard", cascade = ALL)
    private List<BusinessCardTag> businessCardTags = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public BusinessCard(Boolean isPublic, Boolean isRepresent, String frontTemplateColor, String backTemplateColor, User user) {
        this.isPublic = isPublic;
        this.isRepresent = isRepresent;
        this.frontTemplateColor = frontTemplateColor;
        this.backTemplateColor = backTemplateColor;
        this.user = user;
    }

    public void createQrCode(QRCode qrCode) {
        this.qrCode = qrCode;
    }

    public int updateLikeCount(int likeCount) {
        return this.likeCount = likeCount;
    }

    public void updateRepresent(boolean isRepresent) {
        this.isRepresent = isRepresent;
    }
}
