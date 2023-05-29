package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class GuestBook extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "guest_book_id")
    private Long id;

    private String writer; //방명록 작성자
    private String content; //내용
    private Boolean guestBookLike; //방명록 좋아요 여부

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "card_id")
    private BusinessCard businessCard;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public GuestBook(String writer, String content, Boolean guestBookLike, BusinessCard businessCard, User user) {
        this.writer = writer;
        this.content = content;
        this.guestBookLike = guestBookLike;
        this.user = user;
        this.businessCard = businessCard;
    }

    //방명록 좋아요
    public void like() {
        this.guestBookLike = true;
    }

    //방명록 좋아요 취소
    public void cancelLike() {
        this.guestBookLike = false;
    }

}
