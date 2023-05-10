package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class BusinessCard {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "card_id")
    private Long id;

    private Boolean isPublic; //공개 비공개
    private int likeCount; //좋아요 수

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @OneToOne
    @JoinColumn(name = "qrcode_id")
    private QRCode qrCode;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public BusinessCard(Boolean isPublic, User user) {
        this.isPublic = isPublic;
        this.user = user;
    }

}
