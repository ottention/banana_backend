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
public class Category extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String categoryName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Category(String categoryName, User user) {
        this.categoryName = categoryName;
        this.user = user;
    }

}
