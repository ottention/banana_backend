package com.ottention.banana.service.notification;

import com.ottention.banana.entity.User;
import com.ottention.banana.entity.notification.Notification;
import com.ottention.banana.entity.notification.NotificationType;
import com.ottention.banana.repository.UserRepository;
import com.ottention.banana.repository.notification.NotificationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotificationServiceTest {
    @Autowired
    NotificationService notificationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NotificationRepository notificationRepository;

    @Test
    void 알림_구독_진행() {
        //given
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();
        userRepository.save(user);
        String lastEventId = "";

        //when, then
        Assertions.assertDoesNotThrow(() -> notificationService.subscribe(user.getId(), lastEventId));
    }

    @Test
    void 알림_메세지_전송() {
        //given
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();
        userRepository.save(user);
        String lastEventId = "";
        notificationService.subscribe(user.getId(), lastEventId);

        //when, then
        Assertions.assertDoesNotThrow(() -> notificationService.send(user, "새로운 방명록이 등록되었습니다.", "url", NotificationType.NEW_GUESTBOOK));
    }

    @Test
    void 알림_send() {
        //given
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();
        userRepository.save(user);
        String content = "test send method";
        String url = "test/businesscard/";
        NotificationType type = NotificationType.NEW_GUESTBOOK;

        //when
        Assertions.assertDoesNotThrow(() -> notificationService.send(user, content, url, type));


    }

//    @Test
//    void Notification_생성() {
//        User user = User.builder()
//                .email("a")
//                .nickName("a")
//                .build();
//        userRepository.save(user);
//        String content = "test send method";
//        String url = "test/businesscard/";
//        NotificationType type = NotificationType.NEW_GUESTBOOK;
//
//        Notification test = notificationService.creatNotification(user, type, content, url);
//        Long id = notificationRepository.save(test).getId();
//        System.out.println("id = " + id);
//    }

}