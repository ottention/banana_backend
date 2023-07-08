package com.ottention.banana.service.event;

import com.ottention.banana.dto.request.notification.NotificationRequest;
import com.ottention.banana.entity.User;
import com.ottention.banana.entity.notification.NotificationType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;

@Getter
@Builder
public class BusinessCardLikeEvent {
    private final ApplicationEventPublisher eventPublisher;

    private Long businessCardId;
    private User user;

    public void publishEvent() {
        String content = EventContent.BUSINESS_CARD_LIKE_EVENT.getEventContent();
        String url = "/businessCard/" + businessCardId.intValue();
        eventPublisher.publishEvent(NotificationRequest.builder()
                .user(user)
                .content(content)
                .url(url)
                .type(NotificationType.BUSINESS_CARD_LIKE).build());
    }
}
