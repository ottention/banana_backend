package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public class Tag {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    private String name; //태그명

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public void addTag(BusinessCardTag businessCardTag, BusinessCard businessCard) {
        businessCardTag.setTag(this);
        businessCardTag.setBusinessCard(businessCard);
    }

}
