package com.ottention.banana.dto.request.notification;

import com.ottention.banana.entity.User;
import com.ottention.banana.entity.notification.NotificationType;
import com.ottention.banana.service.event.EventContent;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class NotificationRequest {

    private User user;
    private String content;
    private String url;
    private NotificationType type;


    @Builder
    NotificationRequest(User user, String content, String url, NotificationType type) {
        this.user = user;
        this.content = content;
        this.url = url;
        this.type = type;
    }
}
