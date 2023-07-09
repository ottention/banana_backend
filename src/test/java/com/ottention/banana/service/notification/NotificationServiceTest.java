package com.ottention.banana.service.notification;

import com.ottention.banana.entity.User;
import com.ottention.banana.entity.notification.NotificationType;
import com.ottention.banana.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NotificationServiceTest {
    @Autowired
    NotificationService notificationService;
    @Autowired
    UserRepository userRepository;

    @Test
    void 알림_구독_진행() {
        //given
        User user = userRepository.findById(1L).orElseThrow(IllegalArgumentException::new);
        String lastEventId = "";

        //when, then
        Assertions.assertDoesNotThrow(() -> notificationService.subscribe(user.getId(), lastEventId));
    }

    @Test
    void 알림_메세지_전송() {
        //given
        User user = userRepository.findById(1L).orElseThrow(IllegalArgumentException::new);
        String lastEventId = "";
        notificationService.subscribe(user.getId(), lastEventId);

        //when, then
        Assertions.assertDoesNotThrow(() -> notificationService.send(user, "새로운 방명록이 등록되었습니다.", "url", NotificationType.NEW_GUESTBOOK));

    }

}