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
public class GuestBookLike {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "guestBookLike_id")
    private Long id;

    private Boolean like;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "guestBook_id")
    private GuestBook guestBook;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

}
