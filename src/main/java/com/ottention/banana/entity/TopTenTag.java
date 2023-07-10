package com.ottention.banana.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class TopTenTag extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "topTenTag_id")
    private Long id;

    private String topTenTag;

    public void updateTopTenTag(String topTenTag) {
        this.topTenTag = topTenTag;
    }

}
