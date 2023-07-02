package com.ottention.banana.entity.notification;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Relatedurl {
    private String url;

    public Relatedurl(String url) {
        this.url = url;
    }
}
