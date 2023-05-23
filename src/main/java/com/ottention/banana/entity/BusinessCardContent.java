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

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "content_id")
    private Long id;

    private String content;
    private int width;
    private int height;
    private int xAxis;
    private int yAxis;
    private boolean isFront;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "card_id")
    private BusinessCard businessCard;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public static BusinessCardContent createBusinessCardContent(String content, int width, int height,
                                                                int xAxis, int yAxis, boolean isFront) {
        return BusinessCardContent.builder()
                .content(content)
                .width(width)
                .height(height)
                .xAxis(xAxis)
                .yAxis(yAxis)
                .isFront(isFront)
                .build();
    }

    @Builder
    public BusinessCardContent(String content, int width, int height, int xAxis, int yAxis, boolean isFront) {
        this.content = content;
        this.width = width;
        this.height = height;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.isFront = isFront;
    }

    public void addBusinessCard(BusinessCard businessCard) {
        this.businessCard = businessCard;
    }

}
