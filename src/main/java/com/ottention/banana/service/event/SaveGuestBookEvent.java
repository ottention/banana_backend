package com.ottention.banana.service.event;

import com.ottention.banana.dto.request.notification.NotificationRequest;
import com.ottention.banana.entity.User;
import com.ottention.banana.entity.notification.NotificationType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;

@Getter
@Builder
public class SaveGuestBookEvent {
    private final ApplicationEventPublisher eventPublisher;

    private Long businessCardId;
    private User user;
    private String writerNickName;

    public void publishEvent() {
        String content = writerNickName + "님이 방명록을 남겼습니다.";
        String url = "/banana/businessCard/" + businessCardId.intValue() + "/guestBook";
        eventPublisher.publishEvent(NotificationRequest.builder()
                .user(user)
                .content(content)
                .url(url)
                .type(NotificationType.NEW_GUESTBOOK).build());
    }
}
