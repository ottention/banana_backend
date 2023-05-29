package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class StoredBusinessCard extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "stored_business_card_id")
    private Long id;

    private String name;  //명함 저장명
    private Boolean isBookmarked;  //즐겨찾기 여부

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "business_card_id")
    private BusinessCard businessCard;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;  //명함 저장한 사람

}
