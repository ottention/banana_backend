package com.ottention.banana.service.event;

import com.ottention.banana.dto.request.notification.NotificationRequest;
import com.ottention.banana.entity.User;
import com.ottention.banana.entity.notification.NotificationType;
import com.ottention.banana.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;


@SpringBootTest
class NotificationEventListenerTest {
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    NotificationEventListener listener;
    @Autowired
    UserRepository userRepository;


    @Test
    void 이벤트_리스너_알림_send() {
        //given
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();
        userRepository.save(user);
        String content = "test event listener";
        String url = "/business/";

        NotificationRequest request = NotificationRequest.builder()
                .user(user)
                .content(content)
                .url(url)
                .type(NotificationType.NEW_GUESTBOOK)
                .build();

        Assertions.assertDoesNotThrow(() -> listener.sendPush(request));
    }

    @Test
    void eventListener가_eventPublisher의_요청을_받는다() {
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();
        userRepository.save(user);
        String content = "test event listener";
        String url = "/business/";
        NotificationRequest request = NotificationRequest.builder()
                .user(user)
                .content(content)
                .url(url)
                .type(NotificationType.NEW_GUESTBOOK)
                .build();
        Assertions.assertDoesNotThrow(() -> publisher.publishEvent(request));
    }

}