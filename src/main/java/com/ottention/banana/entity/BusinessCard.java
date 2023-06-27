package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private int likeCount; //좋아요 수

    @OneToOne
    @JoinColumn(name = "qrcode_id")
    private QRCode qrCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public BusinessCard(Boolean isPublic, Boolean isRepresent, User user) {
        this.isPublic = isPublic;
        this.isRepresent = isRepresent;
        this.user = user;
    }

    public void createQrCode(QRCode qrCode) {
        this.qrCode = qrCode;
    }

    public int updateLikeCount(int likeCount) {
        return this.likeCount = likeCount;
    }
}
