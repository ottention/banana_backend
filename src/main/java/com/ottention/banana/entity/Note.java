package com.ottention.banana.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Note extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "note_id")
    private Long id;

    @Size(max=120)  //이모티콘 4byte, 한글 2byte
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "stored_business_card_id")
    private StoredBusinessCard storedBusinessCard;
}
