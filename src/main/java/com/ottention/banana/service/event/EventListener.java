package com.ottention.banana.service.event;

import com.ottention.banana.dto.request.notification.NotificationRequest;
import com.ottention.banana.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventListener {
    private final NotificationService notificationService;

    @TransactionalEventListener
    public void sendPush(NotificationRequest event) {
        notificationService.send(event.getUser(), event.getContent(), event.getUrl(), event.getType());
        log.info(String.format("[ content: " + event.getContent() + ", receiver id : " + event.getUser().getId() + " ]"));
    }
}
