package com.ottention.banana.entity.notification;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationContent {
    @NotBlank(message = "notification content must not be blank")
    private String content;

    public NotificationContent(String content) {
        this.content = content;
    }
}
