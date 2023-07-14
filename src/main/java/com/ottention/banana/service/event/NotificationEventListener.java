package com.ottention.banana.service.event;

import com.ottention.banana.dto.request.notification.NotificationRequest;
import com.ottention.banana.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;

    @EventListener
    public void sendPush(NotificationRequest event) {
        notificationService.send(event.getUser(), event.getContent(), event.getUrl(), event.getType());
        log.info(String.format("[ content: " + event.getContent() + ", receiver id : " + event.getUser().getId() + " ]"));
    }
}
