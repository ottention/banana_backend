package com.ottention.banana.entity.wallet;

import com.ottention.banana.entity.BaseEntity;
import com.ottention.banana.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
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

    @OneToMany(mappedBy = "stored_business_card_id", cascade = ALL)
    private List<StoredBusinessCard> storedBusinessCards = new ArrayList<>();

    public Category(String categoryName, User user) {
        this.categoryName = categoryName;
        this.user = user;
    }

}
