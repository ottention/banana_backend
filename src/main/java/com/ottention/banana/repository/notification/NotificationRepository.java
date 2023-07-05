package com.ottention.banana.repository.notification;

import com.ottention.banana.entity.User;
import com.ottention.banana.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUser(User user);  //알림 전체 조회
}
