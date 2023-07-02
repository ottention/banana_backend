package com.ottention.banana.entity.notification;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class NotificationContent {
    @NotBlank(message = "notification content must not be blank")
    private String content;

    public NotificationContent(String content) {
        this.content = content;
    }
}
