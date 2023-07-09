package com.ottention.banana.dto.response;

import com.ottention.banana.entity.notification.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationResponse {
    private Long id;
    private String content;
    private String url;
    private Boolean isRead;
    private LocalDateTime createdDate;

    @Builder
    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.content = notification.getContent().toString();
        this.url = notification.getUrl().getUrl();
        this.isRead = notification.getIsRead();
        this.createdDate = notification.getCreatedDate();
    }
}
