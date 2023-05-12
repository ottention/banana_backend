package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class BusinessCardContent {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "content_id")
    private Long id;

    private String content;
    private int xAxis;
    private int yAxis;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "card_id")
    private BusinessCard businessCard;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Builder
    public BusinessCardContent(String content, int xAxis, int yAxis) {
        this.content = content;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public void addBusinessCard(BusinessCard businessCard) {
        this.businessCard = businessCard;
    }

}
