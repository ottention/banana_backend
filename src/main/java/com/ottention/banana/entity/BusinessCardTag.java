package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class BusinessCardTag extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "business_card_tag_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "business_card_id")
    private BusinessCard businessCard;

    public void createTag(Tag tag) {
        this.tag = tag;
    }

    public void createBusinessCard(BusinessCard businessCard) {
        this.businessCard = businessCard;
    }
}
