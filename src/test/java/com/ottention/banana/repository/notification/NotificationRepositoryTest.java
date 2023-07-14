package com.ottention.banana.repository.notification;

import com.ottention.banana.entity.*;
import com.ottention.banana.entity.notification.Notification;
import com.ottention.banana.entity.notification.NotificationType;
import com.ottention.banana.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NotificationRepositoryTest {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    void 알림_저장() {
        //given
        String content = "this is the content";
        String url = "/business/";
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user);

        //when
        Notification result = notificationRepository.save(Notification.builder()
                .content(content)
                .url(url)
                .isRead(false)
                .notificationType(NotificationType.BUSINESS_CARD_LIKE)
                .user(user)
                .build());

        //then
        assertThat(result.getContent().getContent()).isEqualTo(content);
    }

}