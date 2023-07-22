package com.ottention.banana.entity;

import com.ottention.banana.exception.DuplicationLikeException;
import com.ottention.banana.exception.ZeroLikesError;
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
    public BusinessCardLike(BusinessCard businessCard, User user) {
        //좋아요 카운트 증가
        businessCard.updateLikeCount(businessCard.getLikeCount() + 1);
        this.businessCard = businessCard;
        this.user = user;
    }

    public int cancelLike() {
        return businessCard.updateLikeCount(businessCard.getLikeCount() - 1);
    }

}
