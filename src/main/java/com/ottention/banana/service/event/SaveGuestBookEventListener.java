package com.ottention.banana.service.event;

import com.ottention.banana.dto.request.notification.NotificationRequest;
import com.ottention.banana.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor //?
public class SaveGuestBookEventListener {
    private final NotificationService notificationService;

    @TransactionalEventListener
    public void sendPush(NotificationRequest event) {
        notificationService.send(event.getUser(), event.getContent(), event.getUrl(), event.getType());
        log.info(String.format("방명록 작성 알림 전송 [ 내용 : " + event.getContent() + ", 수신자 : " + event.getUser().getId() + " ]"));
    }
}
