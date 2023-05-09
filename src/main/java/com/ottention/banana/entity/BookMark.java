package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public class BookMark {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "bookMark_id")
    private Long id;

    private Boolean isBookMark;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "businessCard_id")
    private BusinessCard businessCard;

}
