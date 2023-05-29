package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class BusinessCardLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "business_card_like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "business_card_id")
    private BusinessCard businessCard;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public BusinessCardLike(BusinessCard businessCard, User user, Optional<BusinessCardLike> like) {
        //좋아요 중복 검증
        if (!like.isEmpty()) {
            throw new IllegalArgumentException("좋아요는 한 번만 누를 수 있습니다.");
        }
        //좋아요 카운트 증가
        businessCard.updateLikeCount(businessCard.getLikeCount() + 1);
        this.businessCard = businessCard;
        this.user = user;
    }

}
