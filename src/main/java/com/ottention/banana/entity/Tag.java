package com.ottention.banana.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class Tag extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    private String name; //태그명

    public void updateName(String name) {
        this.name = name;
    }

    public void addTag(BusinessCardTag businessCardTag, BusinessCard businessCard) {
        businessCardTag.createTag(this);
        businessCardTag.createBusinessCard(businessCard);
    }

}
