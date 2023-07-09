package com.ottention.banana.repository.notification;

import com.ottention.banana.entity.notification.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserId(Long userId, Pageable pageable);  //알림 전체 조회
    void deleteAllByUserId(Long userId);  //알림 전체 삭제
}
