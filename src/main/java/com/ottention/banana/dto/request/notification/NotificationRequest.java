package com.ottention.banana.dto.request.notification;

import com.ottention.banana.entity.User;
import com.ottention.banana.entity.notification.NotificationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationRequest {
    private User user;
    private String content;
    private String url;
    private NotificationType type;
}